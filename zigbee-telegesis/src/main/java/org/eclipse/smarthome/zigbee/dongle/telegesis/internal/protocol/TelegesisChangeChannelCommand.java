/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.dongle.telegesis.internal.protocol;


/**
 * Class to implement the Telegesis command <b>Change Channel</b>.
 * <p>
 * Ask all nodes in the network to change their channel. If no channel is specified a random
 * channel out of the channels masked in S00 is picked which wasn’t previously blacklisted
 * because of excessive packet loss (NM:ES REPORT WARNING prompt). Note. The New channel needs
 * to be masked in in S00 for all nodes on the network. Ideally S00 should be identical for all
 * nodes on a network.
 * <p>
 * This class provides methods for processing Telegesis AT API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class TelegesisChangeChannelCommand extends TelegesisFrame implements TelegesisCommand {
    /**
     * Command field
     */
    private Integer channel;

    /**
     *
     * @param channel the channel to set as {@link Integer}
     */
    public void setChannel(Integer channel) {
        if (channel < 11 || channel > 26) {
            throw(new IllegalArgumentException("Illegal value passed for channel. Range is 11 to 26."));
        }
        this.channel = channel;
    }

    @Override
    public int[] serialize() {
        // Serialize the command fields
        serializeCommand("AT+CCHANGE");
        if (channel != null) {
            serializeDelimiter();
            serializeInteger(channel);
        }

        return getPayload();
    }

    @Override
    public boolean deserialize(int[] data) {
        // Handle standard status responses (ie. OK / ERROR)
        if (handleIncomingStatus(data)) {
            return true;
        }

        initialiseDeserializer(data);


        return false;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(209);
        // First present the command parameters...
        // Then the responses later if they are available
        builder.append("TelegesisChangeChannelCommand [channel=");
        builder.append(channel);
        if (status != null) {
            builder.append(", status=");
            builder.append(status);
        }
        builder.append(']');
        return builder.toString();
    }
}
