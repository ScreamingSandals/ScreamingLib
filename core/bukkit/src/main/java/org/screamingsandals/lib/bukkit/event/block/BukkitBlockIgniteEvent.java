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
import org.screamingsandals.lib.block.Blocks;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.BasicEntity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.block.BlockIgniteEvent;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitBlockIgniteEvent implements BlockIgniteEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final org.bukkit.event.block.BlockIgniteEvent event;

    // Internal cache
    private Block block;
    private IgniteCause igniteCause;
    private Block ignitingBlock;
    private boolean ignitingBlockConverted;
    private BasicEntity ignitingEntity;
    private boolean ignitingEntityConverted;

    @Override
    public @NotNull Block block() {
        if (block == null) {
            block = Blocks.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public @NotNull IgniteCause igniteCause() {
        if (igniteCause == null) {
            igniteCause = IgniteCause.valueOf(event.getCause().toString());
        }
        return igniteCause;
    }

    @Override
    public @Nullable Block ignitingBlock() {
        if (!ignitingBlockConverted) {
            if (event.getIgnitingBlock() != null) {
                ignitingBlock = Blocks.wrapBlock(event.getIgnitingBlock());
            }
            ignitingBlockConverted = true;
        }
        return ignitingBlock;
    }

    @Override
    public @Nullable BasicEntity ignitingEntity() {
        if (!ignitingEntityConverted) {
            if (event.getIgnitingEntity() != null) {
                ignitingEntity = Entities.wrapEntity(event.getIgnitingEntity()).orElseThrow();
            }
            ignitingEntityConverted = true;
        }
        return ignitingEntity;
    }
}
