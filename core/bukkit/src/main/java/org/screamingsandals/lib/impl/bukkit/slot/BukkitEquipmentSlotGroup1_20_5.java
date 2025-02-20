/*
 * Copyright 2025 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.slot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.slot.EquipmentSlotGroup;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class BukkitEquipmentSlotGroup1_20_5 extends BasicWrapper<org.bukkit.inventory.EquipmentSlotGroup> implements EquipmentSlotGroup {
    public BukkitEquipmentSlotGroup1_20_5(@NotNull org.bukkit.inventory.EquipmentSlotGroup wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.toString();
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof org.bukkit.inventory.EquipmentSlotGroup || object instanceof EquipmentSlotGroup) {
            return equals(object);
        }
        return equals(EquipmentSlotGroup.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public @NotNull ResourceLocation location() {
        return ResourceLocation.of(wrappedObject.toString());
    }
}
