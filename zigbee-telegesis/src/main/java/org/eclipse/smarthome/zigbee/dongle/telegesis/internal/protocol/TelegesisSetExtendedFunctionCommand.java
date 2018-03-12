/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.dongle.telegesis.internal.protocol;


/**
 * Class to implement the Telegesis command <b>Set Extended Function</b>.
 * <p>
 * Sets the Extended Function configuration
 * <p>
 * This class provides methods for processing Telegesis AT API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class TelegesisSetExtendedFunctionCommand extends TelegesisFrame implements TelegesisCommand {
    /**
     * Command field
     */
    private Integer configuration;

    /**
     *
     * @param configuration the configuration to set as {@link Integer}
     */
    public void setConfiguration(Integer configuration) {
        this.configuration = configuration;
    }

    @Override
    public int[] serialize() {
        // Serialize the command fields
        serializeCommand("ATS10=");
        serializeInt16(configuration);

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
        final StringBuilder builder = new StringBuilder(215);
        // First present the command parameters...
        // Then the responses later if they are available
        builder.append("TelegesisSetExtendedFunctionCommand [configuration=");
        builder.append(String.format("%04X", configuration));
        if (status != null) {
            builder.append(", status=");
            builder.append(status);
        }
        builder.append(']');
        return builder.toString();
    }
}
