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

import lombok.*;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SVillagerCareerChangeEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitVillagerCareerChangeEvent implements SVillagerCareerChangeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final VillagerCareerChangeEvent event;

    // Internal cache
    private EntityBasic entity;
    private ChangeReason reason;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public Profession getProfession() {
        return Profession.valueOf(event.getProfession().name());
    }

    @Override
    public void setProfession(Profession profession) {
        event.setProfession(Villager.Profession.valueOf(profession.name()));
    }

    @Override
    public ChangeReason getReason() {
        if (reason == null) {
            reason = ChangeReason.valueOf(event.getReason().name());
        }
        return reason;
    }
}
