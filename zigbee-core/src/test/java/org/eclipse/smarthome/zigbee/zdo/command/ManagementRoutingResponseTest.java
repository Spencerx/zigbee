/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.zdo.command;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.smarthome.zigbee.CommandTest;
import org.eclipse.smarthome.zigbee.serialization.DefaultDeserializer;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldDeserializer;
import org.eclipse.smarthome.zigbee.zdo.field.RoutingTable;
import org.eclipse.smarthome.zigbee.zdo.field.RoutingTable.DiscoveryState;
import org.junit.Test;

/**
 *
 * @author Chris Jackson
 *
 */
public class ManagementRoutingResponseTest extends CommandTest {

    @Test
    public void testReceive() {
        // Short response - ie not extended
        int[] packet = getPacketData("00 00 01 00 01 2A 2F 00 35 38");

        ManagementRoutingResponse routingResponse = new ManagementRoutingResponse();

        DefaultDeserializer deserializer = new DefaultDeserializer(packet);
        ZclFieldDeserializer fieldDeserializer = new ZclFieldDeserializer(deserializer);

        routingResponse.deserialize(fieldDeserializer);

        System.out.println(routingResponse);

        assertEquals(1, (int) routingResponse.getRoutingTableEntries());
        assertEquals(0, (int) routingResponse.getStartIndex());

        List<RoutingTable> routes = routingResponse.getRoutingTableList();

        assertEquals(1, routes.size());

        assertEquals(12074, (int) routes.get(0).getDestinationAddress());
        assertEquals(14389, (int) routes.get(0).getNextHopAddress());
        assertEquals(DiscoveryState.ACTIVE, routes.get(0).getStatus());
    }
}
