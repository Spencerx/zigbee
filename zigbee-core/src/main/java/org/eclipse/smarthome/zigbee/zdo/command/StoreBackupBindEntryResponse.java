/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.zdo.command;

import org.eclipse.smarthome.zigbee.zcl.ZclFieldDeserializer;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldSerializer;
import org.eclipse.smarthome.zigbee.zcl.protocol.ZclDataType;
import org.eclipse.smarthome.zigbee.zdo.ZdoResponse;
import org.eclipse.smarthome.zigbee.zdo.ZdoStatus;

/**
 * Store Backup Bind Entry Response value object class.
 * <p>
 * The Store_Bkup_Bind_Entry_rsp is generated from a backup binding table cache
 * device in response to a Store_Bkup_Bind_Entry_req from a primary binding table
 * cache, and contains the status of the request. This command shall be unicast to the
 * requesting device. If the remote device is not a backup binding table cache, it shall
 * return a status of NOT_SUPPORTED. If the originator of the request is not
 * recognized as a primary binding table cache, it shall return a status of
 * INV_REQUESTTYPE. Otherwise, the backup binding table cache shall add the
 * binding entry to its binding table and return a status of SUCCESS. If there is no
 * room, it shall return a status of TABLE_FULL.
 * <p>
 * Code is auto-generated. Modifications may be overwritten!
 */
public class StoreBackupBindEntryResponse extends ZdoResponse {
    /**
     * Default constructor.
     */
    public StoreBackupBindEntryResponse() {
        clusterId = 0x8025;
    }

    @Override
    public void serialize(final ZclFieldSerializer serializer) {
        super.serialize(serializer);

        serializer.serialize(status, ZclDataType.ZDO_STATUS);
    }

    @Override
    public void deserialize(final ZclFieldDeserializer deserializer) {
        super.deserialize(deserializer);

        status = (ZdoStatus) deserializer.deserialize(ZclDataType.ZDO_STATUS);
        if (status != ZdoStatus.SUCCESS) {
            // Don't read the full response if we have an error
            return;
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(57);
        builder.append("StoreBackupBindEntryResponse [");
        builder.append(super.toString());
        builder.append(", status=");
        builder.append(status);
        builder.append(']');
        return builder.toString();
    }

}
