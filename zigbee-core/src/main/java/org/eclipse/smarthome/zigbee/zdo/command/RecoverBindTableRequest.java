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
 * Recover Bind Table Request value object class.
 * <p>
 * The Recover_Bind_Table_req is generated from a local primary binding table
 * cache and sent to a remote backup binding table cache device when it wants a
 * complete restore of the binding table. The destination addressing mode for this
 * request is unicast.
 * <p>
 * Code is auto-generated. Modifications may be overwritten!
 */
public class RecoverBindTableRequest extends ZdoRequest {
    /**
     * Default constructor.
     */
    public RecoverBindTableRequest() {
        clusterId = 0x0028;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(26);
        builder.append("RecoverBindTableRequest [");
        builder.append(super.toString());
        builder.append(']');
        return builder.toString();
    }

}
