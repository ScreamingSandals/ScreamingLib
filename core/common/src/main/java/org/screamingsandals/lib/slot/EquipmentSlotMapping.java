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

package org.screamingsandals.lib.slot;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.EquipmentSlotHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;

@AbstractService
public abstract class EquipmentSlotMapping extends AbstractTypeMapper<EquipmentSlotHolder> {
    private static EquipmentSlotMapping equipmentSlotMapping;

    protected final BidirectionalConverter<EquipmentSlotHolder> equipmentSlotConverter = BidirectionalConverter.<EquipmentSlotHolder>build()
            .registerP2W(EquipmentSlotHolder.class, e -> e)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return EquipmentSlotHolderSerializer.INSTANCE.deserialize(EquipmentSlotHolder.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    @ApiStatus.Internal
    public EquipmentSlotMapping() {
        if (equipmentSlotMapping != null) {
            throw new UnsupportedOperationException("EquipmentSlotMapping is already initialized.");
        }

        equipmentSlotMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @OfMethodAlternative(value = EquipmentSlotHolder.class, methodName = "ofNullable")
    @Contract("null -> null")
    public static @Nullable EquipmentSlotHolder resolve(@Nullable Object slot) {
        if (equipmentSlotMapping == null) {
            throw new UnsupportedOperationException("EquipmentSlotMapping is not initialized yet.");
        }

        if (slot == null) {
            return null;
        }

        return equipmentSlotMapping.equipmentSlotConverter.convertOptional(slot).or(() -> equipmentSlotMapping.resolveFromMapping(slot)).orElse(null);
    }

    @OfMethodAlternative(value = EquipmentSlotHolder.class, methodName = "all")
    public static @NotNull List<@NotNull EquipmentSlotHolder> getValues() {
        if (equipmentSlotMapping == null) {
            throw new UnsupportedOperationException("EquipmentSlotMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(equipmentSlotMapping.values);
    }

    @OnPostConstruct
    public void legacyMapping() {
        // Vanilla <-> Bukkit
        mapAlias("MAIN_HAND", "HAND");
        mapAlias("OFF_HAND", "OFF_HAND");
        mapAlias("BOOTS", "FEET");
        mapAlias("LEGGINGS", "LEGS");
        mapAlias("CHESTPLATE", "CHEST");
        mapAlias("HELMET", "HEAD");
    }
}
