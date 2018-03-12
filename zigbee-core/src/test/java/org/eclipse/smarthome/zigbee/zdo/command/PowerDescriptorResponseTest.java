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
import org.eclipse.smarthome.zigbee.serialization.DefaultDeserializer;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldDeserializer;
import org.eclipse.smarthome.zigbee.zdo.ZdoStatus;
import org.eclipse.smarthome.zigbee.zdo.field.PowerDescriptor;
import org.eclipse.smarthome.zigbee.zdo.field.PowerDescriptor.CurrentPowerModeType;
import org.eclipse.smarthome.zigbee.zdo.field.PowerDescriptor.PowerLevelType;
import org.eclipse.smarthome.zigbee.zdo.field.PowerDescriptor.PowerSourceType;
import org.junit.Test;

/**
 *
 * @author Chris Jackson
 *
 */
public class PowerDescriptorResponseTest extends CommandTest {

    @Test
    public void testReceive() {
        int[] packet = getPacketData("00 00 00 00 10 C1");

        PowerDescriptorResponse descriptorResponse = new PowerDescriptorResponse();

        DefaultDeserializer deserializer = new DefaultDeserializer(packet);
        ZclFieldDeserializer fieldDeserializer = new ZclFieldDeserializer(deserializer);

        descriptorResponse.deserialize(fieldDeserializer);

        System.out.println(descriptorResponse);

        PowerDescriptor powerDescriptor = descriptorResponse.getPowerDescriptor();
        assertEquals(ZdoStatus.SUCCESS, descriptorResponse.getStatus());

        assertEquals(PowerLevelType.FULL, powerDescriptor.getPowerLevel());
        assertEquals(CurrentPowerModeType.RECEIVER_ON_IDLE, powerDescriptor.getCurrentPowerMode());
        assertEquals(PowerSourceType.MAINS, powerDescriptor.getCurrentPowerSource());
    }

    @Test
    public void testReceiveNotSupported() {
        int[] packet = getPacketData("84 84");

        PowerDescriptorResponse descriptorResponse = new PowerDescriptorResponse();

        DefaultDeserializer deserializer = new DefaultDeserializer(packet);
        ZclFieldDeserializer fieldDeserializer = new ZclFieldDeserializer(deserializer);

        descriptorResponse.deserialize(fieldDeserializer);

        System.out.println(descriptorResponse);
        assertEquals(ZdoStatus.NOT_SUPPORTED, descriptorResponse.getStatus());

        PowerDescriptor powerDescriptor = descriptorResponse.getPowerDescriptor();
        assertEquals(null, powerDescriptor);
    }
}
