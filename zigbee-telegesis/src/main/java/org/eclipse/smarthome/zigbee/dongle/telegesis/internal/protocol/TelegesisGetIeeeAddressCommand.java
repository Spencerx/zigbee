/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.dongle.telegesis.internal.protocol;

import org.eclipse.smarthome.zigbee.IeeeAddress;

/**
 * Class to implement the Telegesis command <b>Get Ieee Address</b>.
 * <p>
 * Gets the local node’s unique EUI64 identifier
 * <p>
 * This class provides methods for processing Telegesis AT API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class TelegesisGetIeeeAddressCommand extends TelegesisFrame implements TelegesisCommand {
    /**
     * Response field
     */
    private IeeeAddress ieeeAddress;

    /**
     *
     * @return the ieeeAddress as {@link IeeeAddress}
     */
    public IeeeAddress getIeeeAddress() {
        return ieeeAddress;
    }

    @Override
    public int[] serialize() {
        // Serialize the command fields
        serializeCommand("ATS04?");

        return getPayload();
    }

    @Override
    public boolean deserialize(int[] data) {
        // Handle standard status responses (ie. OK / ERROR)
        if (handleIncomingStatus(data)) {
            return true;
        }

        initialiseDeserializer(data);

        // Deserialize the fields for the response

        // Deserialize field "ieee address"
        ieeeAddress = deserializeIeeeAddress();

        return false;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(210);
        builder.append("TelegesisGetIeeeAddressCommand [ieeeAddress=");
        builder.append(ieeeAddress);
        if (status != null) {
            builder.append(", status=");
            builder.append(status);
        }
        builder.append(']');
        return builder.toString();
    }
}
