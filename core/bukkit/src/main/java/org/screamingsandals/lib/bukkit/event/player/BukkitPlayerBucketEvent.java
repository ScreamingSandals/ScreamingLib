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

package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.event.player.PlayerBucketFillEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.block.Blocks;
import org.screamingsandals.lib.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.PlayerBucketEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.BlockFace;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerBucketEvent implements PlayerBucketEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerBucketEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable Block block;
    private @Nullable Block blockClicked;
    private @Nullable BlockFace blockFace;
    private @Nullable ItemTypeHolder bucket;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
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
    public @NotNull Block blockClicked() {
        if (blockClicked == null) {
            blockClicked = Blocks.wrapBlock(event.getBlockClicked());
        }
        return blockClicked;
    }

    @Override
    public @NotNull BlockFace blockFace() {
        if (blockFace == null) {
            blockFace = BlockFace.valueOf(event.getBlockFace().name());
        }
        return blockFace;
    }

    @Override
    public @NotNull ItemTypeHolder bucket() {
        if (bucket == null) {
            bucket = ItemTypeHolder.of(event.getBucket());
        }
        return bucket;
    }

    @Override
    public @Nullable ItemStack item() {
        return event.getItemStack() != null ? new BukkitItem(event.getItemStack()) : null;
    }

    @Override
    public void item(@Nullable ItemStack item) {
        event.setItemStack(item == null ? null : item.as(org.bukkit.inventory.ItemStack.class));
    }

    @Override
    public @NotNull Action action() {
        return  event instanceof PlayerBucketFillEvent ? Action.FILL : Action.EMPTY;
    }
}
