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

package org.screamingsandals.lib.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.world.Location;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Represents an entity with identifier {@code minecraft:item}.
 */
public interface ItemEntity extends Entity {

    @NotNull ItemStack getItem();

    void setItem(@NotNull ItemStack stack);

    int getPickupDelay();

    /**
     * Sets the pickup delay.
     *
     * @param delay
     * @param timeUnit
     */
    void setPickupDelay(int delay, @NotNull TimeUnit timeUnit);

    boolean isPickable();

    void setPickable(boolean pickable);

    boolean isMergeable();

    void setMergeable(boolean mergeable);

    float getMergeRange();

    void setMergeRange(float mergeRange);

    long getSpawnTime();

    static @Nullable ItemEntity dropItem(@NotNull ItemStack item, @NotNull Location location) {
        return Entities.dropItem(item, location);
    }

    static @Nullable ItemEntity dropItem(@NotNull ItemStack item, @NotNull Location location, @Nullable Consumer<? super @NotNull ItemEntity> preSpawnFunction) {
        return Entities.dropItem(item, location, preSpawnFunction);
    }
}
