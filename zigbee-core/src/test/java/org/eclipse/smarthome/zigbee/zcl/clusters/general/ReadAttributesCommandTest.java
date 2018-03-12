/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.zcl.clusters.general;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.eclipse.smarthome.zigbee.CommandTest;
import org.eclipse.smarthome.zigbee.ZigBeeEndpointAddress;
import org.eclipse.smarthome.zigbee.serialization.DefaultSerializer;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldSerializer;
import org.junit.Test;

/**
 *
 * @author Chris Jackson
 *
 */
public class ReadAttributesCommandTest extends CommandTest {

    @Test
    public void testSingle() {
        int[] packet = getPacketData("04 00");

        ReadAttributesCommand command = new ReadAttributesCommand();
        command.setClusterId(0);
        command.setDestinationAddress(new ZigBeeEndpointAddress(57337, 3));
        command.setIdentifiers(Arrays.asList(4));
        command.setTransactionId(1);
        DefaultSerializer serializer = new DefaultSerializer();
        ZclFieldSerializer fieldSerializer = new ZclFieldSerializer(serializer);

        command.serialize(fieldSerializer);
        assertTrue(Arrays.equals(packet, serializer.getPayload()));
    }

}
