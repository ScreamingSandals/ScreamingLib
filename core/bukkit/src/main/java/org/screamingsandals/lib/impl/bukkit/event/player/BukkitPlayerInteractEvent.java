/*
 * Copyright 2024 ScreamingSandals
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

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockPlacement;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItemView;
import org.screamingsandals.lib.event.player.PlayerInteractEvent;
import org.screamingsandals.lib.item.ItemStackView;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.utils.BlockFace;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerInteractEvent implements PlayerInteractEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerInteractEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable ItemStackView item;
    private boolean itemCached;
    private @Nullable Action action;
    private @Nullable BlockFace blockFace;
    private @Nullable BlockPlacement clickedBlock;
    private boolean clickedBlockCached;
    private @Nullable EquipmentSlot hand;
    private boolean handCached;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @Nullable ItemStackView item() {
        if (!itemCached) {
            if (event.getItem() != null) {
                item = new BukkitItemView(event.getItem());
            }
            itemCached = true;
        }
        return item;
    }

    @Override
    public @NotNull Action action() {
        if (action == null) {
            action = Action.convert(event.getAction().name());
        }
        return action;
    }

    @Override
    public @Nullable BlockPlacement clickedBlock() {
        if (!clickedBlockCached) {
            if (event.getClickedBlock() != null) {
                clickedBlock = new BukkitBlockPlacement(event.getClickedBlock());
            }
            clickedBlockCached = true;
        }
        return clickedBlock;
    }

    @Override
    public @NotNull BlockFace blockFace() {
        if (blockFace == null) {
            blockFace = BlockFace.valueOf(event.getBlockFace().name());
        }
        return blockFace;
    }

    @Override
    public @NotNull Result useClickedBlock() {
        return Result.convert(event.useInteractedBlock().name());
    }

    @Override
    public void useClickedBlock(@NotNull Result useClickedBlock) {
        event.setUseInteractedBlock(Event.Result.valueOf(useClickedBlock.name()));
    }

    @Override
    public @NotNull Result useItemInHand() {
        return Result.convert(event.useItemInHand().name());
    }

    @Override
    public void useItemInHand(@NotNull Result useItemInHand) {
        event.setUseItemInHand(Event.Result.valueOf(useItemInHand.name()));
    }

    @Override
    public @Nullable EquipmentSlot hand() {
        if (!handCached) {
            if (BukkitFeature.OFF_HAND.isSupported()) {
                if (event.getHand() != null) {
                    hand = EquipmentSlot.of(event.getHand());
                }
                handCached = true;
            } else {
                // 1.8.8
                hand = event.getAction() == org.bukkit.event.block.Action.PHYSICAL ? null : EquipmentSlot.of("main_hand");
                handCached = true;
            }
        }
        return hand;
    }
}
