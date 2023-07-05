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

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.event.player.PlayerInventoryOpenEvent;
import org.screamingsandals.lib.player.Player;

import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerInventoryOpenEvent implements PlayerInventoryOpenEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull InventoryOpenEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable Container topInventory;
    private @Nullable Container bottomInventory;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer((org.bukkit.entity.Player) event.getPlayer());
        }
        return player;
    }

    @Override
    public @NotNull Container topInventory() {
        if (topInventory == null) {
            topInventory = Objects.requireNonNull(ContainerFactory.wrapContainer(event.getInventory()));
        }
        return topInventory;
    }

    @Override
    public @NotNull Container bottomInventory() {
        if (bottomInventory == null) {
            bottomInventory = Objects.requireNonNull(ContainerFactory.wrapContainer(event.getView().getBottomInventory()));
        }
        return bottomInventory;
    }
}
