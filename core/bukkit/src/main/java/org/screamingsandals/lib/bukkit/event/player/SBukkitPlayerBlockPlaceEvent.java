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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.SPlayerBlockPlaceEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableCollectionLinkedToCollection;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

import java.util.Collection;
import java.util.List;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class SBukkitPlayerBlockPlaceEvent implements SPlayerBlockPlaceEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final BlockPlaceEvent event;

    // Internal cache;
    private PlayerWrapper player;
    private Collection<BlockStateHolder> replacedBlockStates;
    private PlayerWrapper.Hand playerHand;
    private BlockHolder block;
    private BlockStateHolder replacedBlockState;
    private Item itemInHand;

    @Override
    public Collection<BlockStateHolder> replacedBlockStates() {
        if (replacedBlockStates == null) {
            if (event instanceof BlockMultiPlaceEvent) {
                replacedBlockStates = new ImmutableCollectionLinkedToCollection<>(
                        ((BlockMultiPlaceEvent) event).getReplacedBlockStates(),
                        blockStateHolder -> blockStateHolder.as(BlockState.class),
                        blockState -> BlockStateMapper.<BlockStateHolder>wrapBlockState(blockState).orElseThrow()
                );
            } else {
                replacedBlockStates = List.of(replacedBlockState());
            }
        }
        return replacedBlockStates;
    }

    @Override
    public PlayerWrapper.Hand playerHand() {
        if (playerHand == null) {
            playerHand = PlayerMapper.wrapHand(event.getHand());
        }
        return playerHand;
    }

    @Override
    public BlockHolder block() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public BlockStateHolder replacedBlockState() {
        if (replacedBlockState == null) {
            replacedBlockState = BlockStateMapper.<BlockStateHolder>wrapBlockState(event.getBlockReplacedState()).orElseThrow();
        }
        return replacedBlockState;
    }

    @Override
    public Item itemInHand() {
        if (itemInHand == null) {
            itemInHand = new BukkitItem(event.getItemInHand());
        }
        return itemInHand;
    }

    @Override
    public @NotNull PlayerWrapper player() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }
}
