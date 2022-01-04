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
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.event.player.SPlayerInventoryCloseEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerInventoryCloseEvent implements SPlayerInventoryCloseEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final InventoryCloseEvent event;

    // Internal cache
    private PlayerWrapper player;
    private Container topInventory;
    private Container bottomInventory;
    private NamespacedMappingKey reason;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer((Player) event.getPlayer());
        }
        return player;
    }

    @Override
    public Container getTopInventory() {
        if (topInventory == null) {
            topInventory = ContainerFactory.wrapContainer(event.getInventory()).orElseThrow();
        }
        return topInventory;
    }

    @Override
    public Container getBottomInventory() {
        if (bottomInventory == null) {
            bottomInventory = ContainerFactory.wrapContainer(event.getView().getBottomInventory()).orElseThrow();
        }
        return bottomInventory;
    }

    @Override
    public NamespacedMappingKey getReason() {
        if (reason == null) {
            reason = NamespacedMappingKey.of(event.getReason().name());
        }
        return reason;
    }
}
