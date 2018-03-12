/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.zcl.field;

import org.eclipse.smarthome.zigbee.serialization.ZigBeeDeserializer;
import org.eclipse.smarthome.zigbee.serialization.ZigBeeSerializer;
import org.eclipse.smarthome.zigbee.zcl.ZclListItemField;
import org.eclipse.smarthome.zigbee.zcl.protocol.ZclDataType;

/**
 * Unsigned 8 Bit Integer field.
 *
 * @author Tommi S.E. Laukkanen
 * @author Chris Jackson
 */
public class Unsigned8BitInteger implements ZclListItemField {
    /**
     * The attribute identifier.
     */
    private int value;

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void serialize(final ZigBeeSerializer serializer) {
        serializer.appendZigBeeType(value, ZclDataType.UNSIGNED_8_BIT_INTEGER);
    }

    @Override
    public void deserialize(final ZigBeeDeserializer deserializer) {
        value = (int) deserializer.readZigBeeType(ZclDataType.UNSIGNED_8_BIT_INTEGER);
    }

    @Override
    public String toString() {
        return "Unsigned 8 Bit Integer: value=" + value;
    }
}
