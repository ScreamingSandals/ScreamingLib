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
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.block.state.BlockSnapshot;
import org.screamingsandals.lib.block.state.BlockSnapshots;
import org.screamingsandals.lib.bukkit.block.BukkitBlock;
import org.screamingsandals.lib.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.PlayerBlockPlaceEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.utils.ImmutableCollectionLinkedToCollection;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

import java.util.Collection;
import java.util.List;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitPlayerBlockPlaceEvent implements PlayerBlockPlaceEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull BlockPlaceEvent event;

    // Internal cache;
    private @Nullable Player player;
    private @Nullable Collection<@NotNull BlockSnapshot> replacedBlockStates;
    private @Nullable EquipmentSlot playerHand;
    private @Nullable Block block;
    private @Nullable BlockSnapshot replacedBlockState;
    private @Nullable ItemStack itemInHand;

    @Override
    public @NotNull Collection<@NotNull BlockSnapshot> replacedBlockStates() {
        if (replacedBlockStates == null) {
            if (event instanceof BlockMultiPlaceEvent) {
                replacedBlockStates = new ImmutableCollectionLinkedToCollection<>(
                        ((BlockMultiPlaceEvent) event).getReplacedBlockStates(),
                        blockStateHolder -> blockStateHolder.as(BlockState.class),
                        blockState -> BlockSnapshots.<BlockSnapshot>wrapBlockState(blockState).orElseThrow()
                );
            } else {
                replacedBlockStates = List.of(replacedBlockState());
            }
        }
        return replacedBlockStates;
    }

    @Override
    public @NotNull EquipmentSlot playerHand() {
        if (playerHand == null) {
            try {
                playerHand = EquipmentSlot.of(event.getHand());
            } catch (Throwable ignored) {
                playerHand = EquipmentSlot.of("main_hand"); // 1.8.8
            }
        }
        return playerHand;
    }

    @Override
    public @NotNull Block block() {
        if (block == null) {
            block = new BukkitBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public @NotNull BlockSnapshot replacedBlockState() {
        if (replacedBlockState == null) {
            replacedBlockState = BlockSnapshots.<BlockSnapshot>wrapBlockState(event.getBlockReplacedState()).orElseThrow();
        }
        return replacedBlockState;
    }

    @Override
    public @NotNull ItemStack itemInHand() {
        if (itemInHand == null) {
            itemInHand = new BukkitItem(event.getItemInHand());
        }
        return itemInHand;
    }

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
        }
        return player;
    }
}
