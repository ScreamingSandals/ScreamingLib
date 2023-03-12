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

package org.screamingsandals.lib.bukkit.slot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.ResourceLocation;

import java.util.Arrays;

public class BukkitEquipmentSlotType extends BasicWrapper<org.bukkit.inventory.EquipmentSlot> implements EquipmentSlot {
    public BukkitEquipmentSlotType(@NotNull org.bukkit.inventory.EquipmentSlot wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof org.bukkit.inventory.EquipmentSlot || object instanceof EquipmentSlot) {
            return equals(object);
        }
        return equals(EquipmentSlot.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public @NotNull ResourceLocation location() {
        return ResourceLocation.of(wrappedObject.name());
    }
}
