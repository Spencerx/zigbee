/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.zsmartsystems.zigbee.dongle.ember.internal.ezsp.command;

import com.zsmartsystems.zigbee.dongle.ember.internal.ezsp.EzspFrameResponse;
import com.zsmartsystems.zigbee.dongle.ember.internal.ezsp.structure.EmberCurrentSecurityState;
import com.zsmartsystems.zigbee.dongle.ember.internal.ezsp.structure.EmberStatus;

/**
 * Class to implement the Ember EZSP command <b>getCurrentSecurityState</b>.
 * <p>
 * Gets the current security state that is being used by a device that is joined in the network.
 * <p>
 * This class provides methods for processing EZSP commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class EzspGetCurrentSecurityStateResponse extends EzspFrameResponse {
    public static int FRAME_ID = 0x69;

    /**
     * The success or failure code of the operation.
     * <p>
     * EZSP type is <i>EmberStatus</i> - Java type is {@link EmberStatus}
     */
    private EmberStatus status;

    /**
     * The security configuration in use by the stack.
     * <p>
     * EZSP type is <i>EmberCurrentSecurityState</i> - Java type is {@link EmberCurrentSecurityState}
     */
    private EmberCurrentSecurityState state;

    /**
     * Response and Handler constructor
     */
    public EzspGetCurrentSecurityStateResponse(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        // Deserialize the fields
        status = deserializer.deserializeEmberStatus();
        state = deserializer.deserializeEmberCurrentSecurityState();
    }

    /**
     * The success or failure code of the operation.
     * <p>
     * EZSP type is <i>EmberStatus</i> - Java type is {@link EmberStatus}
     *
     * @return the current status as {@link EmberStatus}
     */
    public EmberStatus getStatus() {
        return status;
    }

    /**
     * The success or failure code of the operation.
     *
     * @param status the status to set as {@link EmberStatus}
     */
    public void setStatus(EmberStatus status) {
        this.status = status;
    }

    /**
     * The security configuration in use by the stack.
     * <p>
     * EZSP type is <i>EmberCurrentSecurityState</i> - Java type is {@link EmberCurrentSecurityState}
     *
     * @return the current state as {@link EmberCurrentSecurityState}
     */
    public EmberCurrentSecurityState getState() {
        return state;
    }

    /**
     * The security configuration in use by the stack.
     *
     * @param state the state to set as {@link EmberCurrentSecurityState}
     */
    public void setState(EmberCurrentSecurityState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(88);
        builder.append("EzspGetCurrentSecurityStateResponse [status=");
        builder.append(status);
        builder.append(", state=");
        builder.append(state);
        builder.append(']');
        return builder.toString();
    }
}
