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
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockDropItemEvent;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.block.SBlockDropItemEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitBlockDropItemEvent implements SBlockDropItemEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final BlockDropItemEvent event;

    // Internal cache
    private PlayerWrapper player;
    private BlockStateHolder blockState;
    private Collection<EntityItem> items;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public BlockStateHolder getBlockState() {
        if (blockState == null) {
            blockState = BlockStateMapper.wrapBlockState(event.getBlockState()).orElseThrow();
        }
        return blockState;
    }

    @Override
    public Collection<EntityItem> getItems() {
        if (items == null) {
            items = new CollectionLinkedToCollection<>(event.getItems(), entityItem -> entityItem.as(Item.class), item -> EntityMapper.<EntityItem>wrapEntity(item).orElseThrow());
        }
        return items;
    }
}
