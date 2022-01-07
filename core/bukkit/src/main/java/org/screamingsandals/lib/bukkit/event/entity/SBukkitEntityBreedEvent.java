/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.bukkit.event.entity.EntityBreedEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityBreedEvent;
import org.screamingsandals.lib.item.Item;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityBreedEvent implements SEntityBreedEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityBreedEvent event;

    // Internal cache
    private EntityBasic entity;
    private EntityBasic mother;
    private EntityBasic father;
    @Nullable
    private EntityBasic breeder;
    private boolean breederCached;
    @Nullable
    private Item bredWith;
    private boolean bredWithCached;

    @Override
    public EntityBasic entity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public EntityBasic mother() {
        if (mother == null) {
            mother = EntityMapper.wrapEntity(event.getMother()).orElseThrow();
        }
        return mother;
    }

    @Override
    public EntityBasic father() {
        if (father == null) {
            father = EntityMapper.wrapEntity(event.getFather()).orElseThrow();
        }
        return father;
    }

    @Override
    @Nullable
    public EntityBasic breeder() {
        if (!breederCached) {
            if (event.getBreeder() != null) {
                breeder = EntityMapper.wrapEntity(event.getBreeder()).orElseThrow();
            }
            breederCached = true;
        }
        return breeder;
    }

    @Override
    @Nullable
    public Item bredWith() {
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
