/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.attribute;

import com.iamceph.resulter.core.pack.ProtoWrapper;
import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.utils.ProtoItemAttribute;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.UUID;

@Data
public class ItemAttributeHolder implements Wrapper, ProtoWrapper<ProtoItemAttribute> {
    private final AttributeTypeHolder type;
    private final UUID uuid;
    private final String name;
    private final double amount;
    private final AttributeModifierHolder.Operation operation;
    @Nullable
    private final EquipmentSlotHolder slot;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T as(Class<T> type) {
        return AttributeMapping.convertItemAttributeHolder(this, type);
    }

    @Override
    public ProtoItemAttribute asProto() {
        final var builder = ProtoItemAttribute.newBuilder()
                .setType(type.platformName())
                .setUuid(uuid.toString())
                .setName(name)
                .setAmount(amount)
                .setOperation(operation.name());
        if (slot != null && slot.platformName() != null) {
            builder.setSlot(slot.platformName());
        }

        return builder.build();
    }
}
