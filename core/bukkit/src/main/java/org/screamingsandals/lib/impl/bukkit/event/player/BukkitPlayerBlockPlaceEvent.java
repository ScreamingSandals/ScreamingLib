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

package org.screamingsandals.lib.impl.bukkit.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.block.snapshot.BlockSnapshot;
import org.screamingsandals.lib.impl.block.snapshot.BlockSnapshots;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockPlacement;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.PlayerBlockPlaceEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.impl.utils.collections.ImmutableCollectionLinkedToCollection;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerBlockPlaceEvent implements PlayerBlockPlaceEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull BlockPlaceEvent event;

    // Internal cache;
    private @Nullable Player player;
    private @Nullable Collection<@NotNull BlockSnapshot> replacedBlockStates;
    private @Nullable EquipmentSlot playerHand;
    private @Nullable BlockPlacement block;
    private @Nullable BlockSnapshot replacedBlockState;
    private @Nullable ItemStack itemInHand;

    @Override
    public @NotNull Collection<@NotNull BlockSnapshot> replacedBlockStates() {
        if (replacedBlockStates == null) {
            if (event instanceof BlockMultiPlaceEvent) {
                replacedBlockStates = new ImmutableCollectionLinkedToCollection<>(
                        ((BlockMultiPlaceEvent) event).getReplacedBlockStates(),
                        blockStateHolder -> blockStateHolder.as(BlockState.class),
                        blockState -> Objects.requireNonNull(BlockSnapshots.wrapBlockSnapshot(blockState))
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
            if (BukkitFeature.OFF_HAND.isSupported()) {
                playerHand = EquipmentSlot.of(event.getHand());
            } else {
                playerHand = EquipmentSlot.of("main_hand"); // 1.8.8
            }
        }
        return playerHand;
    }

    @Override
    public @NotNull BlockPlacement block() {
        if (block == null) {
            block = new BukkitBlockPlacement(event.getBlock());
        }
        return block;
    }

    @Override
    public @NotNull BlockSnapshot replacedBlockState() {
        if (replacedBlockState == null) {
            replacedBlockState = Objects.requireNonNull(BlockSnapshots.wrapBlockSnapshot(event.getBlockReplacedState()));
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
