/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.dongle.xbee.internal.protocol;


/**
 * Class to implement the XBee command <b>Get Extended PAN ID</b>.
 * <p>
 * AT Command <b>OP</b></p>Read the 64-bit extended PAN ID. The OP value reflects the
 * operating extended PAN ID where the
device is running. If ID > 0, OP equals ID
 * <p>
 * This class provides methods for processing XBee API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class XBeeGetExtendedPanIdCommand extends XBeeFrame implements XBeeCommand {
    /**
     */
    private Integer frameId;

    /**
     *
     * @param frameId the frameId to set as {@link Integer}
     */
    public void setFrameId(Integer frameId) {
        this.frameId = frameId;
    }

    @Override
    public int[] serialize() {
        // Serialize the command fields
        serializeCommand(0x08);
        serializeInt8(frameId);
        serializeAtCommand("OP");

        return getPayload();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(297);
        // First present the command parameters...
        // Then the responses later if they are available
        builder.append("XBeeGetExtendedPanIdCommand [frameId=");
        builder.append(frameId);
        builder.append(']');
        return builder.toString();
    }
}
