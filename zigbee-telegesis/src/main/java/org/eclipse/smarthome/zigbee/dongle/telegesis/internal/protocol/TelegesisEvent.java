/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.dongle.telegesis.internal.protocol;

/**
 * Interface for the Telegesis event.
 * An event is an unsolicited message coming from the Telegesis stick identified with a specific prompt.
 *
 * @author Chris Jackson
 *
 */
public interface TelegesisEvent {

    /**
     * Deserialize an incoming data packet.
     */
    public void deserialize(int[] data);
}
