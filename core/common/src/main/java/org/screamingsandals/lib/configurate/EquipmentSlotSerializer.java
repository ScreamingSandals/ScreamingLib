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

package org.screamingsandals.lib.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class EquipmentSlotSerializer implements TypeSerializer<EquipmentSlot> {
    public static final @NotNull EquipmentSlotSerializer INSTANCE = new EquipmentSlotSerializer();

    @Override
    public @NotNull EquipmentSlot deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            return EquipmentSlot.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable EquipmentSlot obj, @NotNull ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.location().asString());
    }
}