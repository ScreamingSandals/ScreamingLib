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

package org.screamingsandals.lib.impl.bukkit.entity.villager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.villager.VillagerType;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class BukkitVillagerType1_8 implements VillagerType {
    private static final @NotNull ResourceLocation PLAINS = ResourceLocation.of("minecraft", "plains");

    @Override
    public @NotNull String platformName() {
        return PLAINS.path();
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof VillagerType) {
            return this == object;
        }
        return this == VillagerType.ofNullable(object);
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public @NotNull ResourceLocation location() {
        return PLAINS;
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Object raw() {
        throw new UnsupportedOperationException();
    }
}
