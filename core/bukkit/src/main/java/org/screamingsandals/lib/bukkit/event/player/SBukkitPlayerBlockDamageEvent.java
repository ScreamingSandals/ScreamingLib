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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.event.block.BlockDamageEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.SPlayerBlockDamageEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerBlockDamageEvent implements SPlayerBlockDamageEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final BlockDamageEvent event;

    // Internal cache
    private PlayerWrapper player;
    private BlockHolder block;
    private Item itemInHand;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public Item getItemInHand() {
        if (itemInHand == null) {
            itemInHand = new BukkitItem(event.getItemInHand());
        }
        return itemInHand;
    }

    @Override
    public boolean isInstantBreak() {
        return event.getInstaBreak();
    }

    @Override
    public void setInstantBreak(boolean instantBreak) {
        event.setInstaBreak(instantBreak);
    }
}
