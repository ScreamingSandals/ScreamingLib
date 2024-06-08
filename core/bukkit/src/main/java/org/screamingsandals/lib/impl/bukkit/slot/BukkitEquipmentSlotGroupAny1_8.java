/*
 * Copyright 2024 ScreamingSandals
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
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class BukkitEquipmentSlotGroupAny1_8 implements EquipmentSlotGroup {
    public static final @NotNull EquipmentSlotGroup INSTANCE = new BukkitEquipmentSlotGroupAny1_8();
    private static final @NotNull Object RAW = new Object();

    private BukkitEquipmentSlotGroupAny1_8() {
    }

    @Override
    public @NotNull String platformName() {
        return "any";
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object == this) {
            return true;
        }
        return EquipmentSlotGroup.ofNullable(object) == this;
    }

    @Override
    public boolean is(@Nullable Object @NotNull ... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public @NotNull Object raw() {
        return RAW;
    }

    @Override
    public @NotNull ResourceLocation location() {
        return ResourceLocation.of("minecraft", "any");
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        throw new UnsupportedOperationException("Cannot convert to " + type.getSimpleName());
    }
}
