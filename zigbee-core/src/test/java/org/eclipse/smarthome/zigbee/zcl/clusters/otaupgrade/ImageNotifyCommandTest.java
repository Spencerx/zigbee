/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.zcl.clusters.otaupgrade;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.eclipse.smarthome.zigbee.CommandTest;
import org.eclipse.smarthome.zigbee.ZigBeeEndpointAddress;
import org.eclipse.smarthome.zigbee.serialization.DefaultSerializer;
import org.eclipse.smarthome.zigbee.serialization.ZigBeeSerializer;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldSerializer;
import org.junit.Test;

/**
 *
 * @author Chris Jackson
 *
 */
public class ImageNotifyCommandTest extends CommandTest {

    @Test
    public void testSend() {
        ImageNotifyCommand command = new ImageNotifyCommand();
        command.setSourceAddress(new ZigBeeEndpointAddress(0, 1));
        command.setDestinationAddress(new ZigBeeEndpointAddress(57337, 3));
        command.setImageType(6);
        command.setQueryJitter(72);
        command.setManufacturerCode(4364);
        command.setNewFileVersion(16909063);
        System.out.println(command);

        ZigBeeSerializer serializer = new DefaultSerializer();
        ZclFieldSerializer fieldSerializer = new ZclFieldSerializer(serializer);
        command.setPayloadType(0);
        command.serialize(fieldSerializer);
        assertTrue(Arrays.equals(getPacketData("00 48"), serializer.getPayload()));

        serializer = new DefaultSerializer();
        fieldSerializer = new ZclFieldSerializer(serializer);
        command.setPayloadType(1);
        command.serialize(fieldSerializer);
        assertTrue(Arrays.equals(getPacketData("01 48 0C 11"), serializer.getPayload()));

        serializer = new DefaultSerializer();
        fieldSerializer = new ZclFieldSerializer(serializer);
        command.setPayloadType(1);
        command.serialize(fieldSerializer);
        assertTrue(Arrays.equals(getPacketData("01 48 0C 11"), serializer.getPayload()));

        serializer = new DefaultSerializer();
        fieldSerializer = new ZclFieldSerializer(serializer);
        command.setPayloadType(2);
        command.serialize(fieldSerializer);
        assertTrue(Arrays.equals(getPacketData("02 48 0C 11 06 00"), serializer.getPayload()));

        serializer = new DefaultSerializer();
        fieldSerializer = new ZclFieldSerializer(serializer);
        command.setPayloadType(3);
        command.serialize(fieldSerializer);
        assertTrue(Arrays.equals(getPacketData("03 48 0C 11 06 00 07 03 02 01"), serializer.getPayload()));
    }

}
