/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.zdo;

import org.eclipse.smarthome.zigbee.ZigBeeCommand;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldDeserializer;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldSerializer;
import org.eclipse.smarthome.zigbee.zcl.protocol.ZclDataType;

/**
 * Base class for value object classes holding ZDO commands.
 *
 * @author Chris Jackson
 */
public abstract class ZdoCommand extends ZigBeeCommand {

    @Override
    public void serialize(ZclFieldSerializer serializer) {
        serializer.serialize(0, ZclDataType.UNSIGNED_8_BIT_INTEGER);
    }

    @Override
    public void deserialize(final ZclFieldDeserializer deserializer) {
        deserializer.deserialize(ZclDataType.UNSIGNED_8_BIT_INTEGER);
    }
}
