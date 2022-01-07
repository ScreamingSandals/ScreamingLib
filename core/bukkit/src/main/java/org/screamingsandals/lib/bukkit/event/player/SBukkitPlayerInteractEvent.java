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

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.NoAutoCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItemView;
import org.screamingsandals.lib.event.player.SPlayerInteractEvent;
import org.screamingsandals.lib.item.ItemView;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.utils.BlockFace;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerInteractEvent implements SPlayerInteractEvent, NoAutoCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerInteractEvent event;

    // Internal cache
    private PlayerWrapper player;
    private ItemView item;
    private boolean itemCached;
    private Action action;
    private BlockFace blockFace;
    private BlockHolder clickedBlock;
    private boolean clickedBlockCached;
    private EquipmentSlotHolder hand;
    private boolean handCached;

    @Override
    public PlayerWrapper player() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @Nullable ItemView item() {
        if (!itemCached) {
            if (event.getItem() != null) {
                item = new BukkitItemView(event.getItem());
            }
            itemCached = true;
        }
        return item;
    }

    @Override
    public Action action() {
        if (action == null) {
            action = Action.convert(event.getAction().name());
        }
        return action;
    }

    @Override
    public @Nullable BlockHolder clickedBlock() {
        if (!clickedBlockCached) {
            if (event.getClickedBlock() != null) {
                clickedBlock = BlockMapper.wrapBlock(event.getClickedBlock());
            }
            clickedBlockCached = true;
        }
        return clickedBlock;
    }

    @Override
    public BlockFace blockFace() {
        if (blockFace == null) {
            blockFace = BlockFace.valueOf(event.getBlockFace().name());
        }
        return blockFace;
    }

    @Override
    public Result useClickedBlock() {
        return Result.convert(event.useInteractedBlock().name());
    }

    @Override
    public void useClickedBlock(Result useClickedBlock) {
        event.setUseInteractedBlock(Event.Result.valueOf(useClickedBlock.name()));
    }

    @Override
    public Result useItemInHand() {
        return Result.convert(event.useItemInHand().name());
    }

    @Override
    public void useItemInHand(Result useItemInHand) {
        event.setUseItemInHand(Event.Result.valueOf(useItemInHand.name()));
    }

    @Override
    public @Nullable EquipmentSlotHolder hand() {
        if (!handCached) {
            if (event.getHand() != null) {
                hand = EquipmentSlotHolder.of(event.getHand());
            }
            handCached = true;
        }
        return hand;
    }
}
