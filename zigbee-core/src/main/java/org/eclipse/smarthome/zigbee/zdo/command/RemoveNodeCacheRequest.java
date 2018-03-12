/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.zdo.command;

import org.eclipse.smarthome.zigbee.zdo.ZdoRequest;

/**
 * Remove Node Cache Request value object class.
 * <p>
 * The Remove_node_cache_req is provided to enable ZigBee devices on the
 * network to request removal of discovery cache information for a specified ZigBee
 * end device from a Primary Discovery Cache device. The effect of a successful
 * Remove_node_cache_req is to undo a previously successful Discovery_store_req
 * and additionally remove any cache information stored on behalf of the specified
 * ZigBee end device on the Primary Discovery Cache device.
 * <p>
 * Code is auto-generated. Modifications may be overwritten!
 */
public class RemoveNodeCacheRequest extends ZdoRequest {
    /**
     * Default constructor.
     */
    public RemoveNodeCacheRequest() {
        clusterId = 0x001B;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(25);
        builder.append("RemoveNodeCacheRequest [");
        builder.append(super.toString());
        builder.append(']');
        return builder.toString();
    }

}