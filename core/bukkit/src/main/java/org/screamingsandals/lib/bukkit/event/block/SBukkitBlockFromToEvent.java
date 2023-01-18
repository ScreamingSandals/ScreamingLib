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

import org.bukkit.event.block.BlockFromToEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.SBlockFromToEvent;
import org.screamingsandals.lib.utils.BlockFace;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitBlockFromToEvent implements SBlockFromToEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull BlockFromToEvent event;

    // Internal cache
    private @Nullable BlockHolder sourceBlock;
    private @Nullable BlockHolder facedBlock;
    private @Nullable BlockFace face;

    @Override
    public @NotNull BlockHolder sourceBlock() {
        if (sourceBlock == null) {
            sourceBlock = BlockMapper.wrapBlock(event.getBlock());
        }
        return sourceBlock;
    }

    @Override
    public @NotNull BlockHolder facedBlock() {
        if (facedBlock == null) {
            facedBlock = BlockMapper.wrapBlock(event.getToBlock());
        }
        return facedBlock;
    }

    @Override
    public @NotNull BlockFace face() {
        if (face == null) {
            face = BlockFace.valueOf(event.getFace().name());
        }
        return face;
    }
}
