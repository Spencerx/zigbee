/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.zcl.clusters.doorlock;

import org.eclipse.smarthome.zigbee.zcl.ZclCommand;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldDeserializer;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldSerializer;
import org.eclipse.smarthome.zigbee.zcl.protocol.ZclCommandDirection;
import org.eclipse.smarthome.zigbee.zcl.protocol.ZclDataType;

/**
 * Lock Door Command value object class.
 * <p>
 * Cluster: <b>Door Lock</b>. Command is sent <b>TO</b> the server.
 * This command is a <b>specific</b> command used for the Door Lock cluster.
 * <p>
 * Code is auto-generated. Modifications may be overwritten!
 */
public class LockDoorCommand extends ZclCommand {
    /**
     * Pin code command message field.
     */
    private String pinCode;

    /**
     * Default constructor.
     */
    public LockDoorCommand() {
        genericCommand = false;
        clusterId = 257;
        commandId = 0;
        commandDirection = ZclCommandDirection.CLIENT_TO_SERVER;
    }

    /**
     * Gets Pin code.
     *
     * @return the Pin code
     */
    public String getPinCode() {
        return pinCode;
    }

    /**
     * Sets Pin code.
     *
     * @param pinCode the Pin code
     */
    public void setPinCode(final String pinCode) {
        this.pinCode = pinCode;
    }

    @Override
    public void serialize(final ZclFieldSerializer serializer) {
        serializer.serialize(pinCode, ZclDataType.OCTET_STRING);
    }

    @Override
    public void deserialize(final ZclFieldDeserializer deserializer) {
        pinCode = (String) deserializer.deserialize(ZclDataType.OCTET_STRING);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(45);
        builder.append("LockDoorCommand [");
        builder.append(super.toString());
        builder.append(", pinCode=");
        builder.append(pinCode);
        builder.append(']');
        return builder.toString();
    }

}
