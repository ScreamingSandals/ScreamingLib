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

package org.screamingsandals.lib.impl.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.entity.EntityBreedEvent;
import org.screamingsandals.lib.item.ItemStack;

import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitEntityBreedEvent implements EntityBreedEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.entity.EntityBreedEvent event;

    // Internal cache
    private @Nullable Entity entity;
    private @Nullable Entity mother;
    private @Nullable Entity father;
    private @Nullable Entity breeder;
    private boolean breederCached;
    private @Nullable ItemStack bredWith;
    private boolean bredWithCached;

    @Override
    public @NotNull Entity entity() {
        if (entity == null) {
            entity = Objects.requireNonNull(Entities.wrapEntity(event.getEntity()));
        }
        return entity;
    }

    @Override
    public @NotNull Entity mother() {
        if (mother == null) {
            mother = Objects.requireNonNull(Entities.wrapEntity(event.getMother()));
        }
        return mother;
    }

    @Override
    public @NotNull Entity father() {
        if (father == null) {
            father = Objects.requireNonNull(Entities.wrapEntity(event.getFather()));
        }
        return father;
    }

    @Override
    public @Nullable Entity breeder() {
        if (!breederCached) {
            if (event.getBreeder() != null) {
                breeder = Objects.requireNonNull(Entities.wrapEntity(event.getBreeder()));
            }
            breederCached = true;
        }
        return breeder;
    }

    @Override
    public @Nullable ItemStack bredWith() {
        if (!bredWithCached) {
            if (event.getBredWith() != null) {
                bredWith = new BukkitItem(event.getBredWith());
            }
            bredWithCached = true;
        }
        return bredWith;
    }

    @Override
    public int experience() {
        return event.getExperience();
    }

    @Override
    public void experience(int experience) {
        event.setExperience(experience);
    }
}
