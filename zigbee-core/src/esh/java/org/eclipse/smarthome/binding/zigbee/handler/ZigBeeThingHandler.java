/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.binding.zigbee.handler;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.binding.zigbee.ZigBeeBindingConstants;
import org.eclipse.smarthome.binding.zigbee.discovery.ZigBeeNodePropertyDiscoverer;
import org.eclipse.smarthome.binding.zigbee.internal.converter.ZigBeeBaseChannelConverter;
import org.eclipse.smarthome.binding.zigbee.internal.converter.ZigBeeChannelConverterFactory;
import org.eclipse.smarthome.config.core.ConfigDescription;
import org.eclipse.smarthome.config.core.ConfigDescriptionProvider;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingStatusInfo;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.builder.ThingBuilder;
import org.eclipse.smarthome.core.thing.binding.firmware.Firmware;
import org.eclipse.smarthome.core.thing.binding.firmware.FirmwareUpdateHandler;
import org.eclipse.smarthome.core.thing.binding.firmware.ProgressCallback;
import org.eclipse.smarthome.core.thing.binding.firmware.ProgressStep;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.zigbee.IeeeAddress;
import org.eclipse.smarthome.zigbee.ZigBeeEndpoint;
import org.eclipse.smarthome.zigbee.ZigBeeNetworkNodeListener;
import org.eclipse.smarthome.zigbee.ZigBeeNode;
import org.eclipse.smarthome.zigbee.app.otaserver.ZigBeeOtaFile;
import org.eclipse.smarthome.zigbee.app.otaserver.ZigBeeOtaServer;
import org.eclipse.smarthome.zigbee.app.otaserver.ZigBeeOtaServerStatus;
import org.eclipse.smarthome.zigbee.app.otaserver.ZigBeeOtaStatusCallback;
import org.eclipse.smarthome.zigbee.zcl.clusters.ZclOtaUpgradeCluster;
import org.eclipse.smarthome.zigbee.zdo.field.NeighborTable;
import org.eclipse.smarthome.zigbee.zdo.field.RoutingTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Chris Jackson - Initial Contribution
 *
 */
public class ZigBeeThingHandler extends BaseThingHandler
        implements ZigBeeNetworkNodeListener, FirmwareUpdateHandler, ConfigDescriptionProvider {
    /**
     * Our logger
     */
    private final Logger logger = LoggerFactory.getLogger(ZigBeeThingHandler.class);

    /**
     * The map of all the channels defined for this thing
     */
    private final Map<ChannelUID, ZigBeeBaseChannelConverter> channels = new HashMap<ChannelUID, ZigBeeBaseChannelConverter>();

    /**
     * The {@link IeeeAddress} for this device
     */
    private IeeeAddress nodeIeeeAddress = null;

    private ZigBeeCoordinatorHandler coordinatorHandler;

    private boolean nodeInitialised = false;

    private final Object pollingSync = new Object();
    private ScheduledFuture<?> pollingJob = null;
    private final int POLLING_PERIOD_MIN = 5;
    private final int POLLING_PERIOD_MAX = 86400;
    private final int POLLING_PERIOD_DEFAULT = 1800;
    private int pollingPeriod = POLLING_PERIOD_DEFAULT;

    private boolean firmwareUpdateInProgress = false;

    /**
     * A set of channels that have been linked to items. This is used to ensure we only poll channels that are linked to
     * keep network activity to a minimum.
     */
    private final Set<ChannelUID> thingChannelsPoll = new HashSet<ChannelUID>();

    public ZigBeeThingHandler(Thing zigbeeDevice) {
        super(zigbeeDevice);
    }

    @Override
    public void initialize() {
        final String configAddress = (String) getConfig().get(ZigBeeBindingConstants.CONFIGURATION_MACADDRESS);
        logger.debug("{}: Initializing ZigBee thing handler {}", configAddress, getThing().getUID());

        if (configAddress == null || configAddress.length() == 0) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    ZigBeeBindingConstants.OFFLINE_NO_ADDRESS);
            return;
        }
        nodeIeeeAddress = new IeeeAddress(configAddress);

        updateStatus(ThingStatus.OFFLINE);

        if (getBridge() != null) {
            bridgeStatusChanged(getBridge().getStatusInfo());
        }
    }

    @Override
    public void bridgeStatusChanged(ThingStatusInfo bridgeStatusInfo) {
        logger.debug("{}: Coordinator status changed to {}.", nodeIeeeAddress, bridgeStatusInfo.getStatus());

        if (bridgeStatusInfo.getStatus() != ThingStatus.ONLINE || getBridge() == null) {
            logger.debug("{}: Coordinator is unknown or not online.", nodeIeeeAddress, bridgeStatusInfo.getStatus());
            return;
        }

        logger.debug("{}: Coordinator is ONLINE. Starting device initialisation.", nodeIeeeAddress);

        coordinatorHandler = (ZigBeeCoordinatorHandler) getBridge().getHandler();
        coordinatorHandler.addNetworkNodeListener(this);
        coordinatorHandler.rediscoverNode(nodeIeeeAddress);

        initialiseZigBeeNode();
    }

    private void initialiseZigBeeNode() {
        scheduler.schedule(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                doNodeInitialisation();
                return null;
            }
        }, 10, TimeUnit.MILLISECONDS);
    }

    private synchronized void doNodeInitialisation() {
        if (nodeInitialised) {
            return;
        }

        ZigBeeNode node = coordinatorHandler.getNode(nodeIeeeAddress);
        if (node == null) {
            logger.debug("{}: Node not found - deferring handler initialisation", nodeIeeeAddress);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE, ZigBeeBindingConstants.OFFLINE_NODE_NOT_FOUND);
            return;
        }
        if (!node.isDiscovered()) {
            logger.debug("{}: Node has not finished discovery", nodeIeeeAddress);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE,
                    ZigBeeBindingConstants.OFFLINE_DISCOVERY_INCOMPLETE);
            return;
        }

        logger.debug("{}: Start initialising ZigBee Thing handler", nodeIeeeAddress);

        // Create the channel factory
        ZigBeeChannelConverterFactory factory = new ZigBeeChannelConverterFactory();

        // Create the channels from the device
        // Process all the endpoints for this device and add all channels as derived from the supported clusters
        List<Channel> nodeChannels = new ArrayList<Channel>();
        for (ZigBeeEndpoint endpoint : coordinatorHandler.getNodeEndpoints(nodeIeeeAddress)) {
            logger.debug("{}: Checking endpoint {} channels", nodeIeeeAddress, endpoint.getEndpointId());
            nodeChannels.addAll(factory.getChannels(getThing().getUID(), endpoint));
        }

        logger.debug("{}: Created {} channels", nodeIeeeAddress, nodeChannels.size());
        try {
            pollingPeriod = POLLING_PERIOD_MAX;

            // Check if the channels we've discovered are the same
            List<ChannelUID> oldChannelUidList = new ArrayList<ChannelUID>();
            for (Channel channel : getThing().getChannels()) {
                oldChannelUidList.add(channel.getUID());
            }
            List<ChannelUID> newChannelUidList = new ArrayList<ChannelUID>();
            for (Channel channel : nodeChannels) {
                newChannelUidList.add(channel.getUID());

                // Add the configuration from the existing channel into the new channel
                Channel currentChannel = getThing().getChannel(channel.getUID().toString());
                if (currentChannel != null) {
                    channel.getConfiguration().setProperties(currentChannel.getConfiguration().getProperties());
                }
            }

            if (!newChannelUidList.equals(oldChannelUidList)) {
                logger.debug("{}: Updating thing definition as channels have changed from {} to {}", nodeIeeeAddress,
                        oldChannelUidList, newChannelUidList);
                ThingBuilder thingBuilder = editThing();
                thingBuilder.withChannels(nodeChannels).withConfiguration(getConfig());
                updateThing(thingBuilder.build());
            }

            // Create the channel map to simplify processing incoming events
            for (Channel channel : getThing().getChannels()) {
                // Process the channel properties
                Map<String, String> properties = channel.getProperties();

                ZigBeeBaseChannelConverter handler = factory.createConverter(this, channel, coordinatorHandler,
                        node.getIeeeAddress(),
                        Integer.parseInt(properties.get(ZigBeeBindingConstants.CHANNEL_PROPERTY_ENDPOINT)));
                if (handler == null) {
                    logger.debug("{}: No handler found for {}", nodeIeeeAddress, channel.getUID());
                    continue;
                }

                logger.debug("{}: Initializing channel {} with {}", nodeIeeeAddress, channel.getUID(), handler);
                handler.initializeConverter();

                handler.handleRefresh();

                // TODO: Update the channel configuration from the device if method available
                handler.updateConfiguration(channel.getConfiguration());

                channels.put(channel.getUID(), handler);

                if (handler.getPollingPeriod() < pollingPeriod) {
                    pollingPeriod = handler.getPollingPeriod();
                }
            }
        } catch (Exception e) {
            logger.error("{}: Exception creating channels ", nodeIeeeAddress, e);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.HANDLER_INITIALIZING_ERROR);
            return;
        }
        logger.debug("{}: Channel initialisation complete", nodeIeeeAddress);

        // Update the general properties
        ZigBeeNodePropertyDiscoverer propertyDiscoverer = new ZigBeeNodePropertyDiscoverer();
        propertyDiscoverer.setProperties(editProperties());
        Map<String, String> newProperties = propertyDiscoverer.getProperties(coordinatorHandler, node);
        updateProperties(newProperties);

        // Update the binding table.
        // We're not doing anything with the information here, but we want it up to date so it's ready for use later.
        try {
            if (node.updateBindingTable().get() == false) {
                logger.debug("{}: Error getting binding table", nodeIeeeAddress);
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("{}: Exception getting binding table ", nodeIeeeAddress, e);
        }

        updateStatus(ThingStatus.ONLINE);

        startPolling();

        nodeInitialised = true;

        logger.debug("{}: Done initialising ZigBee Thing handler", nodeIeeeAddress);

        // Save the network state
        coordinatorHandler.serializeNetwork();
    }

    @Override
    public void dispose() {
        logger.debug("{}: Handler dispose.", nodeIeeeAddress);

        stopPolling();

        if (nodeIeeeAddress != null) {
            if (coordinatorHandler != null) {
                coordinatorHandler.removeNetworkNodeListener(this);
            }
            nodeIeeeAddress = null;
        }

        for (ZigBeeBaseChannelConverter channel : channels.values()) {
            channel.disposeConverter();
        }
        channels.clear();

        nodeInitialised = false;
    }

    private void stopPolling() {
        synchronized (pollingSync) {
            if (pollingJob != null) {
                pollingJob.cancel(true);
                pollingJob = null;
                logger.debug("{}: Polling stopped", nodeIeeeAddress);
            }
        }
    }

    /**
     * Start polling channel updates
     */
    private void startPolling() {
        Runnable pollingRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    logger.debug("{}: Polling...", nodeIeeeAddress);

                    for (ChannelUID channelUid : channels.keySet()) {
                        if (!thingChannelsPoll.contains(channelUid)) {
                            // Don't poll if this channel isn't linked
                            continue;
                        }

                        logger.debug("{}: Polling {}", nodeIeeeAddress, channelUid);
                        ZigBeeBaseChannelConverter converter = channels.get(channelUid);
                        if (converter == null) {
                            logger.debug("{}: Polling aborted as no converter found for {}", nodeIeeeAddress,
                                    channelUid);
                        } else {
                            converter.handleRefresh();
                        }
                    }
                } catch (Exception e) {
                    logger.warn("{}: Polling aborted due to exception ", nodeIeeeAddress, e);
                }
            }
        };

        synchronized (pollingSync) {
            stopPolling();

            if (pollingPeriod < POLLING_PERIOD_MIN) {
                logger.debug("{}: Polling period was set below minimum value. Using minimum.", nodeIeeeAddress);
                pollingPeriod = POLLING_PERIOD_MIN;
            }

            if (pollingPeriod > POLLING_PERIOD_MAX) {
                logger.debug("{}: Polling period was set above maximum value. Using maximum.", nodeIeeeAddress);
                pollingPeriod = POLLING_PERIOD_MAX;
            }

            // Polling starts almost immediately to get an immediate refresh
            // Add some random element to the period so that all things aren't synchronised
            int pollingPeriodMs = pollingPeriod * 1000 + new Random().nextInt(pollingPeriod * 100);
            pollingJob = scheduler.scheduleAtFixedRate(pollingRunnable, new Random().nextInt(pollingPeriodMs),
                    pollingPeriodMs, TimeUnit.MILLISECONDS);
            logger.debug("{}: Polling initialised at {}ms", nodeIeeeAddress, pollingPeriodMs);
        }
    }

    @Override
    public void channelLinked(ChannelUID channelUID) {
        logger.debug("{}: Channel {} linked - polling started.", nodeIeeeAddress, channelUID);

        // We keep track of what channels are used and only poll channels that the framework is using
        thingChannelsPoll.add(channelUID);
    }

    @Override
    public void channelUnlinked(ChannelUID channelUID) {
        logger.debug("{}: Channel {} unlinked - polling stopped.", nodeIeeeAddress, channelUID);

        // We keep track of what channels are used and only poll channels that the framework is using
        thingChannelsPoll.remove(channelUID);
    }

    @Override
    public void handleRemoval() {
        coordinatorHandler.leave(nodeIeeeAddress);
        updateStatus(ThingStatus.REMOVED);
    }

    @Override
    public void handleConfigurationUpdate(Map<String, Object> configurationParameters) {
        logger.debug("{}: Configuration received: {}", nodeIeeeAddress, configurationParameters);

        Configuration configuration = editConfiguration();
        for (Entry<String, Object> configurationParameter : configurationParameters.entrySet()) {
            switch (configurationParameter.getKey()) {
                case ZigBeeBindingConstants.CONFIGURATION_JOINENABLE:
                    coordinatorHandler.permitJoin(nodeIeeeAddress, 60);
                    break;
                case ZigBeeBindingConstants.CONFIGURATION_LEAVE:
                    coordinatorHandler.leave(nodeIeeeAddress);
                    break;
                default:
                    logger.warn("{}: Unhandled configuration parameter {}.", nodeIeeeAddress,
                            configurationParameter.getKey());
                    break;
            }
        }

        // Persist changes
        updateConfiguration(configuration);
    }

    @Override
    public void handleCommand(final ChannelUID channelUID, final Command command) {
        logger.debug("{}: Command for channel {} --> {}", nodeIeeeAddress, channelUID, command);

        // Check that we have a coordinator to work through
        if (coordinatorHandler == null) {
            logger.debug("{}: Coordinator handler not found. Cannot handle command without coordinator.",
                    nodeIeeeAddress);
            updateStatus(ThingStatus.OFFLINE);
            return;
        }

        ZigBeeBaseChannelConverter handler = channels.get(channelUID);
        if (handler == null) {
            logger.debug("{}: No handler found for {}", nodeIeeeAddress, channelUID);
            return;
        }

        Runnable commandHandler = new Runnable() {
            @Override
            public void run() {
                try {
                    if (command == RefreshType.REFRESH) {
                        handler.handleRefresh();
                    } else {
                        handler.handleCommand(command);
                    }
                } catch (Exception e) {
                    logger.debug("{}: Exception sending command to channel {}", nodeIeeeAddress, channelUID, e);
                }
            }
        };
        scheduler.schedule(commandHandler, 0, TimeUnit.MILLISECONDS);
    }

    /**
     * Callback from handlers to update a channel state. This is called from the channel converter when the state
     * changes.
     *
     * @param channel the {@link ChannelUID} to be updated
     * @param state the new {link State}
     */
    public void setChannelState(ChannelUID channel, State state) {
        logger.debug("{}: Updating ZigBee channel state {} to {}", nodeIeeeAddress, channel, state);
        if (firmwareUpdateInProgress) {
            return;
        }
        updateState(channel, state);
        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void nodeAdded(ZigBeeNode node) {
        nodeUpdated(node);
    }

    @Override
    public void nodeUpdated(ZigBeeNode node) {
        // Make sure it's our node that's updated
        if (!node.getIeeeAddress().equals(nodeIeeeAddress)) {
            return;
        }

        logger.debug("{}: Node updated - {}", nodeIeeeAddress, node);

        Map<String, String> properties = editProperties();

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append('[');
        boolean first = true;
        for (NeighborTable neighbor : node.getNeighbors()) {
            if (!first) {
                jsonBuilder.append(',');
            }
            first = false;

            Map<String, Object> object = new HashMap<String, Object>();
            object.put("address", neighbor.getNetworkAddress());
            object.put("macaddress", neighbor.getExtendedAddress());
            object.put("depth", neighbor.getDepth());
            object.put("lqi", neighbor.getLqi());
            object.put("joining", neighbor.getPermitJoining());
            jsonBuilder.append(ZigBeeBindingConstants.propertiesToJson(object));
        }
        jsonBuilder.append(']');
        properties.put(ZigBeeBindingConstants.THING_PROPERTY_NEIGHBORS, jsonBuilder.toString());

        jsonBuilder = new StringBuilder();
        jsonBuilder.append('[');
        first = true;
        for (RoutingTable route : node.getRoutes()) {
            if (!first) {
                jsonBuilder.append(',');
            }
            first = false;

            Map<String, Object> object = new HashMap<String, Object>();
            object.put("destination", route.getDestinationAddress());
            object.put("next_hop", route.getNextHopAddress());
            object.put("state", route.getStatus());
            jsonBuilder.append(ZigBeeBindingConstants.propertiesToJson(object));
        }
        jsonBuilder.append(']');

        properties.put(ZigBeeBindingConstants.THING_PROPERTY_ROUTES, jsonBuilder.toString());
        properties.put(ZigBeeBindingConstants.THING_PROPERTY_ASSOCIATEDDEVICES, node.getAssociatedDevices().toString());
        properties.put(ZigBeeBindingConstants.THING_PROPERTY_LASTUPDATE,
                ZigBeeBindingConstants.getISO8601StringForDate(node.getLastUpdateTime()));
        properties.put(ZigBeeBindingConstants.THING_PROPERTY_NETWORKADDRESS, node.getNetworkAddress().toString());

        updateProperties(properties);

        initialiseZigBeeNode();
    }

    @Override
    public void nodeRemoved(ZigBeeNode node) {
        // Make sure it's our node that's updated
        if (!node.getIeeeAddress().equals(nodeIeeeAddress)) {
            return;
        }

        // Clear some properties
        Map<String, String> properties = editProperties();
        properties.put(ZigBeeBindingConstants.THING_PROPERTY_LASTUPDATE, "");
        properties.put(ZigBeeBindingConstants.THING_PROPERTY_ROUTES, "[]");
        properties.put(ZigBeeBindingConstants.THING_PROPERTY_NEIGHBORS, "[]");
        properties.put(ZigBeeBindingConstants.THING_PROPERTY_ASSOCIATEDDEVICES, "[]");
        updateProperties(properties);

        updateStatus(ThingStatus.OFFLINE);
    }

    @Override
    public Collection<ConfigDescription> getConfigDescriptions(Locale locale) {
        return Collections.emptySet();
    }

    @Override
    public ConfigDescription getConfigDescription(URI uri, Locale locale) {
        if ("channel".equals(uri.getScheme()) == false) {
            return null;
        }

        ChannelUID channelUID = new ChannelUID(uri.getSchemeSpecificPart());

        // Is this a zigbee thing?
        if (!channelUID.getBindingId().equals(ZigBeeBindingConstants.BINDING_ID)) {
            return null;
        }

        // Do we know this channel?
        if (channels.get(channelUID) == null) {
            return null;
        }

        ZigBeeBaseChannelConverter converter = channels.get(channelUID);

        return new ConfigDescription(uri, converter.getConfigDescription());
    }

    @Override
    public void updateFirmware(Firmware firmware, ProgressCallback progressCallback) {
        logger.debug("{}: Update firmware with {}", nodeIeeeAddress, firmware.getVersion());

        // Find an OTA client if the device supports OTA upgrades
        ZigBeeNode node = coordinatorHandler.getNode(nodeIeeeAddress);
        if (node == null) {
            logger.debug("{}: Can't find node", nodeIeeeAddress);
            return;
        }

        ZigBeeOtaServer otaServer = null;
        ZigBeeEndpoint otaEndpoint = null;
        ZclOtaUpgradeCluster otaCluster = null;
        for (ZigBeeEndpoint endpoint : node.getEndpoints()) {
            otaServer = (ZigBeeOtaServer) endpoint.getExtension(ZclOtaUpgradeCluster.CLUSTER_ID);
            if (otaServer != null) {
                break;
            }

            otaCluster = (ZclOtaUpgradeCluster) endpoint.getOutputCluster(ZclOtaUpgradeCluster.CLUSTER_ID);
            if (otaCluster != null) {
                otaEndpoint = endpoint;
                break;
            }
        }

        if (otaServer == null && otaCluster == null) {
            logger.debug("{}: Can't find OTA cluster", nodeIeeeAddress);
            return;
        }

        // Register the OTA server if it's not already registered
        if (otaServer == null && otaEndpoint != null) {
            otaServer = new ZigBeeOtaServer();
            otaEndpoint.addExtension(otaServer);
        } else {
            logger.debug("{}: Can't create OTA server", nodeIeeeAddress);
            return;
        }

        // Set ourselves offline, and prevent going back online
        firmwareUpdateInProgress = true;
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.FIRMWARE_UPDATING);

        // Define the sequence of the firmware update so that external consumers can listen for the progress
        progressCallback.defineSequence(ProgressStep.TRANSFERRING, ProgressStep.REBOOTING);

        ZigBeeOtaFile otaFile = new ZigBeeOtaFile(firmware.getBytes());
        otaServer.setFirmware(otaFile);

        // DOWNLOADING
        progressCallback.next();

        final ZigBeeOtaServer finalOtaServer = otaServer;
        final ZclOtaUpgradeCluster finalOtaCluster = otaCluster;
        otaServer.addListener(new ZigBeeOtaStatusCallback() {
            @Override
            public void otaStatusUpdate(ZigBeeOtaServerStatus status, int percent) {
                logger.debug("{}: OTA transfer status update {}, percent={}", nodeIeeeAddress, status, percent);
                switch (status) {
                    case OTA_WAITING:
                        // DOWNLOADING
                        progressCallback.next();
                        return;
                    case OTA_TRANSFER_IN_PROGRESS:
                        progressCallback.update(percent);
                        return;
                    case OTA_TRANSFER_COMPLETE:
                        // REBOOTING
                        progressCallback.next();
                        progressCallback.update(100);
                        return;
                    case OTA_UPGRADE_COMPLETE:
                        progressCallback.success();
                        break;
                    case OTA_UPGRADE_FAILED:
                        progressCallback.failed("zigbee.firmware.failed");
                        break;
                    case OTA_CANCELLED:
                        progressCallback.canceled();
                        break;
                    default:
                        return;
                }

                // OTA transfer is complete, cancelled or failed
                firmwareUpdateInProgress = false;
                finalOtaServer.cancelUpgrade();

                for (int retry = 0; retry < 3; retry++) {
                    Integer fileVersion = finalOtaCluster.getCurrentFileVersion(Long.MAX_VALUE);
                    if (fileVersion != null) {
                        updateProperty(Thing.PROPERTY_FIRMWARE_VERSION, String.format("%08X", fileVersion));
                        break;
                    } else {
                        logger.debug("{}: OTA firmware request timeout (retry {})", node.getIeeeAddress(), retry);
                    }
                }

                updateStatus(ThingStatus.ONLINE);
            }
        });
    }

    @Override
    public void cancel() {
        logger.debug("{}: Cancel firmware update", nodeIeeeAddress);
    }

    @Override
    public boolean isUpdateExecutable() {
        // Always allow the firmware to be updated
        return true;
    }

}
