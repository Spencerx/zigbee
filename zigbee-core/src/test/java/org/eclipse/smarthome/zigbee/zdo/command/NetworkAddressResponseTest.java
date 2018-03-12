/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.zdo.command;

import static org.junit.Assert.assertEquals;

import org.eclipse.smarthome.zigbee.CommandTest;
import org.eclipse.smarthome.zigbee.IeeeAddress;
import org.eclipse.smarthome.zigbee.serialization.DefaultDeserializer;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldDeserializer;
import org.eclipse.smarthome.zigbee.zdo.ZdoStatus;
import org.junit.Test;

/**
 *
 * @author Chris Jackson
 *
 */
public class NetworkAddressResponseTest extends CommandTest {

    @Test
    public void testReceive() {
        int[] packet = getPacketData("00 00 43 1D A5 00 AA 3E B0 7C 74 3B");

        NetworkAddressResponse addressResponse = new NetworkAddressResponse();

        DefaultDeserializer deserializer = new DefaultDeserializer(packet);
        ZclFieldDeserializer fieldDeserializer = new ZclFieldDeserializer(deserializer);

        addressResponse.deserialize(fieldDeserializer);

        System.out.println(addressResponse);

        assertEquals(new IeeeAddress("7CB03EAA00A51D43"), addressResponse.getIeeeAddrRemoteDev());
        assertEquals(0x8000, (int) addressResponse.getClusterId());
        assertEquals(ZdoStatus.SUCCESS, addressResponse.getStatus());
    }

}
