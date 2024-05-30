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

package org.screamingsandals.lib.impl.bukkit.compat.v1_20_1;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class EntitySpawnCompat {
    /**
     * Provides a way to spawn an entity with pre-spawn callback in 1.11-1.20.1. The overloaded variant
     * {@code spawn(org.bukkit.Location, Class, org.bukkit.util.Consumer)} does not exist anymore since 1.20.2,
     * making it unable to compile compatible version against newer API.
     */
    public static <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> type, @NotNull java.util.function.Consumer<T> consumer) {
        org.bukkit.util.Consumer<T> cons = consumer::accept;
        return location.getWorld().spawn(location, type, cons);
    }

    /**
     * Provides a way to drop an item with pre-spawn callback in 1.11-1.20.1. The overloaded variant
     * {@code spawn(org.bukkit.Location, org.bukkit.inventory.ItemStack, org.bukkit.util.Consumer)} does not exist anymore since 1.20.2,
     * making it unable to compile compatible version against newer API.
     */
    public static @NotNull Item dropItem(@NotNull Location location, @NotNull ItemStack item, @NotNull java.util.function.Consumer<Item> consumer) {
        org.bukkit.util.Consumer<Item> cons = consumer::accept;
        return location.getWorld().dropItem(location, item, cons);
    }
}
