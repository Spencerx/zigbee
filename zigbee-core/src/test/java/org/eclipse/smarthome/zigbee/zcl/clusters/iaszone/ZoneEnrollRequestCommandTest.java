/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.zcl.clusters.iaszone;

import static org.junit.Assert.assertEquals;

import org.eclipse.smarthome.zigbee.CommandTest;
import org.eclipse.smarthome.zigbee.serialization.DefaultDeserializer;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldDeserializer;
import org.eclipse.smarthome.zigbee.zcl.ZclHeader;
import org.junit.Test;

/**
 *
 * @author Chris Jackson
 *
 */
public class ZoneEnrollRequestCommandTest extends CommandTest {

    @Test
    public void test() {
        int[] packet = getPacketData("09 63 01 0D 00 4E 10");

        ZoneEnrollRequestCommand command = new ZoneEnrollRequestCommand();

        DefaultDeserializer deserializer = new DefaultDeserializer(packet);
        ZclFieldDeserializer fieldDeserializer = new ZclFieldDeserializer(deserializer);

        ZclHeader zclHeader = new ZclHeader(fieldDeserializer);
        System.out.println(zclHeader);

        command.deserialize(fieldDeserializer);
        System.out.println(command);

        assertEquals(Integer.valueOf(0x500), command.getClusterId());
        assertEquals(Integer.valueOf(13), command.getZoneType());
        assertEquals(Integer.valueOf(4174), command.getManufacturerCode());
    }

}
