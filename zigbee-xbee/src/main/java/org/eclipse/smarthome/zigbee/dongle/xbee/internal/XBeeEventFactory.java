/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.dongle.xbee.internal;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.smarthome.zigbee.dongle.xbee.internal.protocol.XBeeEvent;
import org.eclipse.smarthome.zigbee.dongle.xbee.internal.protocol.XBeeManyToOneRouteRequestEvent;
import org.eclipse.smarthome.zigbee.dongle.xbee.internal.protocol.XBeeModemStatusEvent;
import org.eclipse.smarthome.zigbee.dongle.xbee.internal.protocol.XBeeReceivePacketEvent;
import org.eclipse.smarthome.zigbee.dongle.xbee.internal.protocol.XBeeReceivePacketExplicitEvent;
import org.eclipse.smarthome.zigbee.dongle.xbee.internal.protocol.XBeeRouteRecordEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Helper factory class to create {@link XBeeEvent} classes.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class XBeeEventFactory {
    private final static Logger logger = LoggerFactory.getLogger(XBeeEventFactory.class);

    private static Map<Integer, Class<?>> events = new ConcurrentHashMap<Integer, Class<?>>();

    static {
        // Define the API commands
        events.put(0x8A, XBeeModemStatusEvent.class);
        events.put(0x90, XBeeReceivePacketEvent.class);
        events.put(0x91, XBeeReceivePacketExplicitEvent.class);
        events.put(0xA1, XBeeRouteRecordEvent.class);
        events.put(0xA3, XBeeManyToOneRouteRequestEvent.class);
    }

    public static XBeeEvent getXBeeFrame(int[] data) {
        Class<?> xbeeClass = events.get(data[2]);

        // No handler found
        if (xbeeClass == null) {
            return null;
        }

        Constructor<?> ctor;
        try {
            ctor = xbeeClass.getConstructor();
            XBeeEvent xbeeFrame = (XBeeEvent) ctor.newInstance();
            xbeeFrame.deserialize(data);
            return xbeeFrame;
        } catch (Exception e) {
            logger.debug("Error creating instance of XBeeEvent", e);
        }

        return null;
    }
}
