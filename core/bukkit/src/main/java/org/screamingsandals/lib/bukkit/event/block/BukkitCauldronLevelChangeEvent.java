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

package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.bukkit.block.BukkitBlock;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.block.CauldronLevelChangeEvent;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitCauldronLevelChangeEvent implements CauldronLevelChangeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final org.bukkit.event.block.@NotNull CauldronLevelChangeEvent event;

    // Internal cache
    private @Nullable Block block;
    private @Nullable Entity entity;
    private boolean entityConverted;
    private @Nullable Reason reason;

    @Override
    public @NotNull Block block() {
        if (block == null) {
            block = new BukkitBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public @Nullable Entity entity() {
        if (!entityConverted) {
            if (event.getEntity() != null) {
                entity = Entities.wrapEntity(event.getEntity()).orElseThrow();
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
    public @NotNull Reason reason() {
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
