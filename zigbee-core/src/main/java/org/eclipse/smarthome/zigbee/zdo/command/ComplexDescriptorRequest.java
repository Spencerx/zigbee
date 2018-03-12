/**
 * Copyright (c) 2016-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.zigbee.zdo.command;

import org.eclipse.smarthome.zigbee.ZigBeeCommand;
import org.eclipse.smarthome.zigbee.ZigBeeTransactionMatcher;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldDeserializer;
import org.eclipse.smarthome.zigbee.zcl.ZclFieldSerializer;
import org.eclipse.smarthome.zigbee.zcl.protocol.ZclDataType;
import org.eclipse.smarthome.zigbee.zdo.ZdoRequest;
import org.eclipse.smarthome.zigbee.zdo.command.ComplexDescriptorResponse;

/**
 * Complex Descriptor Request value object class.
 * <p>
 * The Complex_Desc_req command is generated from a local device wishing to
 * inquire as to the complex descriptor of a remote device. This command shall be
 * unicast either to the remote device itself or to an alternative device that contains
 * the discovery information of the remote device.
 * <p>
 * Code is auto-generated. Modifications may be overwritten!
 */
public class ComplexDescriptorRequest extends ZdoRequest implements ZigBeeTransactionMatcher {
    /**
     * NWKAddrOfInterest command message field.
     */
    private Integer nwkAddrOfInterest;

    /**
     * Default constructor.
     */
    public ComplexDescriptorRequest() {
        clusterId = 0x0010;
    }

    /**
     * Gets NWKAddrOfInterest.
     *
     * @return the NWKAddrOfInterest
     */
    public Integer getNwkAddrOfInterest() {
        return nwkAddrOfInterest;
    }

    /**
     * Sets NWKAddrOfInterest.
     *
     * @param nwkAddrOfInterest the NWKAddrOfInterest
     */
    public void setNwkAddrOfInterest(final Integer nwkAddrOfInterest) {
        this.nwkAddrOfInterest = nwkAddrOfInterest;
    }

    @Override
    public void serialize(final ZclFieldSerializer serializer) {
        super.serialize(serializer);

        serializer.serialize(nwkAddrOfInterest, ZclDataType.NWK_ADDRESS);
    }

    @Override
    public void deserialize(final ZclFieldDeserializer deserializer) {
        super.deserialize(deserializer);

        nwkAddrOfInterest = (Integer) deserializer.deserialize(ZclDataType.NWK_ADDRESS);
    }

    @Override
    public boolean isTransactionMatch(ZigBeeCommand request, ZigBeeCommand response) {
        if (!(response instanceof ComplexDescriptorResponse)) {
            return false;
        }

        return (((ComplexDescriptorRequest) request).getNwkAddrOfInterest()
                .equals(((ComplexDescriptorResponse) response).getNwkAddrOfInterest()));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(64);
        builder.append("ComplexDescriptorRequest [");
        builder.append(super.toString());
        builder.append(", nwkAddrOfInterest=");
        builder.append(nwkAddrOfInterest);
        builder.append(']');
        return builder.toString();
    }

}
