/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.dao;

import org.eclipse.smarthome.zigbee.IeeeAddress;
import org.eclipse.smarthome.zigbee.ZigBeeEndpoint;
import org.eclipse.smarthome.zigbee.ZigBeeNetworkManager;
import org.eclipse.smarthome.zigbee.ZigBeeNode;
import org.eclipse.smarthome.zigbee.transport.ZigBeeTransportTransmit;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author Chris Jackson
 *
 */
public class ZigBeeNodeDaoTest {
    @Test
    public void testSerialize() {
        ZigBeeTransportTransmit mockedTransport = Mockito.mock(ZigBeeTransportTransmit.class);
        ZigBeeNetworkManager networkManager = new ZigBeeNetworkManager(mockedTransport);
        ZigBeeNode node = new ZigBeeNode(networkManager, new IeeeAddress("1234567890ABCDEF"));
        node.setNetworkAddress(12345);

        ZigBeeEndpoint endpoint;
        endpoint = new ZigBeeEndpoint(networkManager, node, 1);
        endpoint.setProfileId(123);
        node.addEndpoint(endpoint);
        endpoint = new ZigBeeEndpoint(networkManager, node, 2);
        endpoint.setProfileId(321);
        node.addEndpoint(endpoint);
        /*
         * ZigBeeNodeDao nodeDao = ZigBeeNodeDao.createFromZigBeeNode(node);
         * assertEquals(new IeeeAddress("1234567890ABCDEF").toString(), nodeDao.getIeeeAddress());
         * assertEquals(Integer.valueOf(12345), nodeDao.getNetworkAddress());
         * 
         * node = ZigBeeNodeDao.createFromZigBeeDao(networkManager, nodeDao);
         * assertEquals(new IeeeAddress("1234567890ABCDEF"), node.getIeeeAddress());
         * assertEquals(Integer.valueOf(12345), node.getNetworkAddress());
         * assertEquals(2, node.getEndpoints().size());
         * 
         * endpoint = node.getEndpoint(1);
         * assertEquals(123, endpoint.getProfileId());
         */
    }
}
