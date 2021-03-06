/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.asterix.dataflow.data.nontagged.comparators;

import org.apache.asterix.om.typecomputer.impl.TypeComputeUtils;
import org.apache.asterix.om.types.IAType;
import org.apache.hyracks.api.dataflow.value.IBinaryComparatorFactory;
import org.apache.hyracks.api.exceptions.HyracksDataException;
import org.apache.hyracks.api.io.IJsonSerializable;
import org.apache.hyracks.api.io.IPersistedResourceRegistry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class AbstractAGenericBinaryComparatorFactory implements IBinaryComparatorFactory {

    private static final long serialVersionUID = 1L;
    // these fields can be null
    protected final IAType leftType;
    protected final IAType rightType;

    AbstractAGenericBinaryComparatorFactory(IAType leftType, IAType rightType) {
        this.leftType = leftType == null ? null : TypeComputeUtils.getActualType(leftType);
        this.rightType = rightType == null ? null : TypeComputeUtils.getActualType(rightType);
    }

    JsonNode convertToJson(IPersistedResourceRegistry registry, Class<? extends IJsonSerializable> clazz, long version)
            throws HyracksDataException {
        ObjectNode jsonNode = registry.getClassIdentifier(clazz, version);
        if (leftType != null) {
            jsonNode.set("leftType", leftType.toJson(registry));
        }
        if (rightType != null) {
            jsonNode.set("rightType", rightType.toJson(registry));
        }
        return jsonNode;
    }

    static IJsonSerializable convertToObject(IPersistedResourceRegistry registry, JsonNode json, boolean asc)
            throws HyracksDataException {
        JsonNode leftNode = json.get("leftType");
        JsonNode rightNode = json.get("rightType");
        IAType leftType = leftNode == null || leftNode.isNull() ? null : (IAType) registry.deserialize(leftNode);
        IAType rightType = rightNode == null || rightNode.isNull() ? null : (IAType) registry.deserialize(rightNode);
        return asc ? new AGenericAscBinaryComparatorFactory(leftType, rightType)
                : new AGenericDescBinaryComparatorFactory(leftType, rightType);
    }
}
