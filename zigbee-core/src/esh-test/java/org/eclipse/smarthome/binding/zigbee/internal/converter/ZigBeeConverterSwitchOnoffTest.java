package org.eclipse.smarthome.binding.zigbee.internal.converter;

import static org.junit.Assert.assertEquals;

import org.eclipse.smarthome.binding.zigbee.handler.ZigBeeCoordinatorHandler;
import org.eclipse.smarthome.binding.zigbee.handler.ZigBeeThingHandler;
import org.eclipse.smarthome.binding.zigbee.internal.converter.ZigBeeConverterSwitchOnoff;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.zigbee.IeeeAddress;
import org.eclipse.smarthome.zigbee.ZigBeeEndpoint;
import org.eclipse.smarthome.zigbee.zcl.ZclAttribute;
import org.eclipse.smarthome.zigbee.zcl.protocol.ZclClusterType;
import org.eclipse.smarthome.zigbee.zcl.protocol.ZclDataType;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * Tests for OnOff converter
 *
 * @author Chris Jackson
 *
 */
public class ZigBeeConverterSwitchOnoffTest {

    @Test
    public void testAttributeUpdated() {
        ZigBeeEndpoint endpoint = Mockito.mock(ZigBeeEndpoint.class);
        ZigBeeCoordinatorHandler coordinatorHandler = Mockito.mock(ZigBeeCoordinatorHandler.class);
        Mockito.when(coordinatorHandler.getEndpoint(Matchers.any(IeeeAddress.class), Matchers.anyInt())).thenReturn(endpoint);

        ZigBeeConverterSwitchOnoff converter = new ZigBeeConverterSwitchOnoff();
        ArgumentCaptor<ChannelUID> channelCapture = ArgumentCaptor.forClass(ChannelUID.class);
        ArgumentCaptor<State> stateCapture = ArgumentCaptor.forClass(State.class);
        ZigBeeThingHandler thingHandler = Mockito.mock(ZigBeeThingHandler.class);
        Channel channel = new Channel(new ChannelUID("a:b:c:d"), "");
        converter.initialize(thingHandler, channel, coordinatorHandler, new IeeeAddress("1234567890ABCDEF"), 1);
        ZclAttribute attribute = new ZclAttribute(ZclClusterType.ON_OFF, 0, "OnOff", ZclDataType.BOOLEAN, false, false,
                false, false);
        converter.attributeUpdated(attribute);
        Mockito.verify(thingHandler, Mockito.times(1)).setChannelState(channelCapture.capture(),
                stateCapture.capture());

        assertEquals(new ChannelUID("a:b:c:d"), channelCapture.getValue());
    }
}
