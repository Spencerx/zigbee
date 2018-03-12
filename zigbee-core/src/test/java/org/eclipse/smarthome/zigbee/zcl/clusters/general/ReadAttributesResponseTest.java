/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.zcl.clusters.general;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.smarthome.zigbee.CommandTest;
import org.eclipse.smarthome.zigbee.serialization.DefaultDeserializer;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldDeserializer;
import org.eclipse.smarthome.zigbee.zcl.ZclStatus;
import org.eclipse.smarthome.zigbee.zcl.field.ReadAttributeStatusRecord;
import org.eclipse.smarthome.zigbee.zcl.protocol.ZclDataType;
import org.junit.Test;

/**
 *
 * @author Chris Jackson
 *
 */
public class ReadAttributesResponseTest extends CommandTest {

    @Test
    public void testReceive() {
        int[] packet = getPacketData("05 00 00 42 06 4C 43 54 30 30 33 21 00 1D");

        ReadAttributesResponse response = new ReadAttributesResponse();

        DefaultDeserializer deserializer = new DefaultDeserializer(packet);
        ZclFieldDeserializer fieldDeserializer = new ZclFieldDeserializer(deserializer);

        response.deserialize(fieldDeserializer);

        System.out.println(response);

        List<ReadAttributeStatusRecord> records = response.getRecords();
        ReadAttributeStatusRecord record = records.get(0);
        assertEquals(ZclDataType.CHARACTER_STRING, record.getAttributeDataType());
        assertEquals(5, record.getAttributeIdentifier());
        assertEquals(ZclStatus.SUCCESS, record.getStatus());
        assertEquals("LCT003", record.getAttributeValue());
    }

    @Test
    public void testReceiveOtaImageStatus() {
        int[] packet = getPacketData("06 00 00 20 02");

        ReadAttributesResponse response = new ReadAttributesResponse();

        DefaultDeserializer deserializer = new DefaultDeserializer(packet);
        ZclFieldDeserializer fieldDeserializer = new ZclFieldDeserializer(deserializer);

        response.deserialize(fieldDeserializer);

        System.out.println(response);

        List<ReadAttributeStatusRecord> records = response.getRecords();
        ReadAttributeStatusRecord record = records.get(0);
        assertEquals(ZclDataType.UNSIGNED_8_BIT_INTEGER, record.getAttributeDataType());
        assertEquals(6, record.getAttributeIdentifier());
        assertEquals(ZclStatus.SUCCESS, record.getStatus());
        assertEquals(2, record.getAttributeValue());
    }

    @Test
    public void testReceiveNull() {
        int[] packet = getPacketData("01 00 86");

        ReadAttributesResponse response = new ReadAttributesResponse();

        DefaultDeserializer deserializer = new DefaultDeserializer(packet);
        ZclFieldDeserializer fieldDeserializer = new ZclFieldDeserializer(deserializer);

        response.deserialize(fieldDeserializer);
        System.out.println(response);

        List<ReadAttributeStatusRecord> records = response.getRecords();
        ReadAttributeStatusRecord record = records.get(0);
        assertEquals(ZclStatus.UNSUPPORTED_ATTRIBUTE, record.getStatus());
    }

}
