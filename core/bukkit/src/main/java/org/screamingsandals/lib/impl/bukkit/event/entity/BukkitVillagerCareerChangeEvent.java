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

package org.screamingsandals.lib.impl.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.villager.Profession;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.entity.VillagerCareerChangeEvent;

import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitVillagerCareerChangeEvent implements VillagerCareerChangeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.entity.VillagerCareerChangeEvent event;

    // Internal cache
    private @Nullable Entity entity;
    private @Nullable ChangeReason reason;

    @Override
    public @NotNull Entity entity() {
        if (entity == null) {
            entity = Objects.requireNonNull(Entities.wrapEntity(event.getEntity()));
        }
        return entity;
    }

    @Override
    public @NotNull Profession profession() {
        return Profession.of(event.getProfession());
    }

    @Override
    public void profession(@NotNull Profession profession) {
        event.setProfession(profession.as(Villager.Profession.class));
    }

    @Override
    public @NotNull ChangeReason changeReason() {
        if (reason == null) {
            reason = ChangeReason.valueOf(event.getReason().name());
        }
        return reason;
    }
}
