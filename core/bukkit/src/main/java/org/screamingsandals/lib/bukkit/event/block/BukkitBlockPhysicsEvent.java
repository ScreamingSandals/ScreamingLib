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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.block.BukkitBlock;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.BlockPhysicsEvent;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitBlockPhysicsEvent implements BlockPhysicsEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.block.BlockPhysicsEvent event;

    // Internal cache
    private @Nullable Block block;
    private @Nullable BlockTypeHolder material;
    private @Nullable Block causingBlock;

    @Override
    public @NotNull Block block() {
        if (block == null) {
            block = new BukkitBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public @NotNull BlockTypeHolder material() {
        if (material == null) {
            material = BlockTypeHolder.of(event.getChangedType());
        }
        return material;
    }

    @Override
    public @NotNull Block causingBlock() {
        if (causingBlock == null) {
            causingBlock = new BukkitBlock(event.getSourceBlock());
        }
        return causingBlock;
    }
}
