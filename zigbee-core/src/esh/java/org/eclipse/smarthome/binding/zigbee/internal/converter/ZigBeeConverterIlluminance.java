/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.binding.zigbee.internal.converter;

import java.math.BigDecimal;

import org.eclipse.smarthome.binding.zigbee.ZigBeeBindingConstants;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.zigbee.ZigBeeEndpoint;
import org.eclipse.smarthome.zigbee.zcl.ZclAttribute;
import org.eclipse.smarthome.zigbee.zcl.ZclAttributeListener;
import org.eclipse.smarthome.zigbee.zcl.clusters.ZclIlluminanceMeasurementCluster;
import org.eclipse.smarthome.zigbee.zcl.protocol.ZclClusterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converter for the illuminance channel
 *
 * @author Chris Jackson - Initial Contribution
 *
 */
public class ZigBeeConverterIlluminance extends ZigBeeBaseChannelConverter implements ZclAttributeListener {
    private Logger logger = LoggerFactory.getLogger(ZigBeeConverterIlluminance.class);

    private ZclIlluminanceMeasurementCluster cluster;

    @Override
    public boolean initializeConverter() {
        cluster = (ZclIlluminanceMeasurementCluster) endpoint
                .getInputCluster(ZclIlluminanceMeasurementCluster.CLUSTER_ID);
        if (cluster == null) {
            logger.error("{}: Error opening device illuminance measurement cluster", endpoint.getIeeeAddress());
            return false;
        }

        cluster.bind();

        // Add a listener, then request the status
        cluster.addAttributeListener(this);

        // Configure reporting - no faster than once per second - no slower than 10 minutes.
        cluster.setMeasuredValueReporting(1, REPORTING_PERIOD_DEFAULT_MAX, 1);
        return true;
    }

    @Override
    public void disposeConverter() {
        cluster.removeAttributeListener(this);
    }

    @Override
    public void handleRefresh() {
        cluster.getMeasuredValue(0);
    }

    @Override
    public Channel getChannel(ThingUID thingUID, ZigBeeEndpoint endpoint) {
        if (endpoint.getInputCluster(ZclIlluminanceMeasurementCluster.CLUSTER_ID) == null) {
            return null;
        }
        return createChannel(thingUID, endpoint, ZigBeeBindingConstants.CHANNEL_ILLUMINANCE_VALUE,
                ZigBeeBindingConstants.ITEM_TYPE_NUMBER, "Illuminance");
    }

    @Override
    public void attributeUpdated(ZclAttribute attribute) {
        logger.debug("{}: ZigBee attribute reports {}", endpoint.getIeeeAddress(), attribute);
        if (attribute.getCluster() == ZclClusterType.ILLUMINANCE_MEASUREMENT
                && attribute.getId() == ZclIlluminanceMeasurementCluster.ATTR_MEASUREDVALUE) {
            Integer value = (Integer) attribute.getLastValue();
            if (value != null) {
                updateChannelState(new DecimalType(BigDecimal.valueOf(value, 2)));
            }
        }
    }
}
