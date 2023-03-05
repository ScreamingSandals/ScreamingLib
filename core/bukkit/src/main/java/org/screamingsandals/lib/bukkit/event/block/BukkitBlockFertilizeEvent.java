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
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.block.Blocks;
import org.screamingsandals.lib.block.state.BlockSnapshot;
import org.screamingsandals.lib.block.state.BlockSnapshots;
import org.screamingsandals.lib.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.BlockFertilizeEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

import java.util.Collection;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitBlockFertilizeEvent implements BlockFertilizeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.block.BlockFertilizeEvent event;

    // Internal cache
    private @Nullable Player player;
    private boolean playerConverted;
    private @Nullable Block block;
    private @Nullable Collection<@NotNull BlockSnapshot> changedBlockStates;

    @Override
    public @Nullable Player player() {
        if (!playerConverted) {
            if (event.getPlayer() != null) {
                player = new BukkitPlayer(event.getPlayer());
            }
            playerConverted = true;
        }
        return player;
    }

    @Override
    public @NotNull Block block() {
        if (block == null) {
            block = Blocks.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public @NotNull Collection<@NotNull BlockSnapshot> changedBlockStates() {
        if (changedBlockStates == null) {
            changedBlockStates = new CollectionLinkedToCollection<>(event.getBlocks(), o -> o.as(BlockState.class), o -> BlockSnapshots.<BlockSnapshot>wrapBlockState(o).orElseThrow());
        }
        return changedBlockStates;
    }
}
