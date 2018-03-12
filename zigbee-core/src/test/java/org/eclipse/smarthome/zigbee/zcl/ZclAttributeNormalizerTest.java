/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.zcl;

import static org.junit.Assert.assertEquals;

import org.eclipse.smarthome.zigbee.zcl.protocol.ZclDataType;
import org.junit.Test;

/**
 *
 * @author Chris Jackson
 *
 */
public class ZclAttributeNormalizerTest {
    @Test
    public void testNormalizeBoolean() {
        ZclAttributeNormalizer normalizer = new ZclAttributeNormalizer();

        assertEquals(Boolean.TRUE, normalizer.normalizeZclData(ZclDataType.BOOLEAN, Integer.valueOf(1)));
        assertEquals(Boolean.TRUE, normalizer.normalizeZclData(ZclDataType.BOOLEAN, Integer.valueOf(100)));
        assertEquals(Boolean.FALSE, normalizer.normalizeZclData(ZclDataType.BOOLEAN, Integer.valueOf(0)));
    }
}
