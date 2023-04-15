/*
 * Copyright 2023 ScreamingSandals
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

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.attribute.Attributes;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.api.Wrapper;

import java.util.UUID;

@Data
public class ItemAttribute implements Wrapper {
    private final @NotNull AttributeType type;
    private final @NotNull UUID uuid;
    private final @NotNull String name;
    private final double amount;
    private final AttributeModifier.@NotNull Operation operation;
    private final @Nullable EquipmentSlot slot;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        return Attributes.convertItemAttributeHolder(this, type);
    }
}
