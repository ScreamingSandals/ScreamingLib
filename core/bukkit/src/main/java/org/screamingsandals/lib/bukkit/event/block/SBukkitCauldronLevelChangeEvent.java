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

package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.block.SCauldronLevelChangeEvent;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitCauldronLevelChangeEvent implements SCauldronLevelChangeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final CauldronLevelChangeEvent event;

    // Internal cache
    private BlockHolder block;
    private EntityBasic entity;
    private boolean entityConverted;
    private Reason reason;

    @Override
    public BlockHolder block() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    @Nullable
    public EntityBasic entity() {
        if (!entityConverted) {
            if (event.getEntity() != null) {
                entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
            }
            entityConverted = true;
        }
        return entity;
    }

    @Override
    public int oldLevel() {
        return event.getOldLevel();
    }

    @Override
    public Reason reason() {
        if (reason == null) {
            reason = Reason.get(event.getReason().name());
        }
        return reason;
    }

    @Override
    public int newLevel() {
        return event.getNewLevel();
    }

    @Override
    public void newLevel(int newLevel) {
        event.setNewLevel(newLevel);
    }
}
