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
 * Class to implement the Telegesis command <b>Device Left Network</b>.
 * <p>
 * A device has left the PAN
 * <p>
 * This class provides methods for processing Telegesis AT API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class TelegesisDeviceLeftNetworkEvent extends TelegesisFrame implements TelegesisEvent {
    /**
     * NODELEFT response field
     */
    private Integer networkAddress;

    /**
     * NODELEFT response field
     */
    private IeeeAddress ieeeAddress;

    /**
     *
     * @return the networkAddress as {@link Integer}
     */
    public Integer getNetworkAddress() {
        return networkAddress;
    }

    /**
     *
     * @return the ieeeAddress as {@link IeeeAddress}
     */
    public IeeeAddress getIeeeAddress() {
        return ieeeAddress;
    }


    @Override
    public void deserialize(int[] data) {
        initialiseDeserializer(data);

        // Deserialize the fields for the "NODELEFT" response
        if (testPrompt(data, "NODELEFT:")) {
            setDeserializer(9);

            // Deserialize field "network address"
            networkAddress = deserializeInt16();
            stepDeserializer();

            // Deserialize field "Ieee Address"
            ieeeAddress = deserializeIeeeAddress();
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(301);
        builder.append("TelegesisDeviceLeftNetworkEvent [networkAddress=");
        builder.append(networkAddress);
        builder.append(", ieeeAddress=");
        builder.append(ieeeAddress);
        builder.append(']');
        return builder.toString();
    }
}
