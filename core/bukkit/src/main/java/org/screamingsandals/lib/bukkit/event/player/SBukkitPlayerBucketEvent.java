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

package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.SPlayerBucketEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BlockFace;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerBucketEvent implements SPlayerBucketEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerBucketEvent event;

    // Internal cache
    private PlayerWrapper player;
    private BlockHolder block;
    private BlockHolder blockClicked;
    private BlockFace blockFace;
    private ItemTypeHolder bucket;

    @Override
    public PlayerWrapper player() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public BlockHolder block() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public BlockHolder blockClicked() {
        if (blockClicked == null) {
            blockClicked = BlockMapper.wrapBlock(event.getBlockClicked());
        }
        return blockClicked;
    }

    @Override
    public BlockFace blockFace() {
        if (blockFace == null) {
            blockFace = BlockFace.valueOf(event.getBlockFace().name());
        }
        return blockFace;
    }

    @Override
    public ItemTypeHolder bucket() {
        if (bucket == null) {
            bucket = ItemTypeHolder.of(event.getBucket());
        }
        return bucket;
    }

    @Override
    @Nullable
    public Item item() {
        return event.getItemStack() != null ? new BukkitItem(event.getItemStack()) : null;
    }

    @Override
    public void item(@Nullable Item item) {
        event.setItemStack(item == null ? null : item.as(ItemStack.class));
    }

    @Override
    public Action action() {
        return  event instanceof PlayerBucketFillEvent ? Action.FILL : Action.EMPTY;
    }
}
