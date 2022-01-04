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
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEnderDragonChangePhaseEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEnderDragonChangePhaseEvent implements SEnderDragonChangePhaseEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EnderDragonChangePhaseEvent event;

    // Internal cache
    private EntityBasic entity;
    @Nullable
    private Phase currentPhase;
    private boolean currentPhaseCached;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    @Nullable
    public Phase getCurrentPhase() {
        if (!currentPhaseCached) {
            if (event.getCurrentPhase() != null) {
                currentPhase = Phase.valueOf(event.getCurrentPhase().name());
            }
            currentPhaseCached = true;
        }
        return currentPhase;
    }

    @Override
    public Phase getNewPhase() {
        return Phase.valueOf(event.getNewPhase().name());
    }

    @Override
    public void setNewPhase(Phase newPhase) {
        event.setNewPhase(EnderDragon.Phase.valueOf(newPhase.name()));
    }
}
