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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.player.PlayerShearEntityEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.slot.EquipmentSlot;

import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerShearEntityEvent implements PlayerShearEntityEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerShearEntityEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable Entity what;
    private @Nullable ItemStack item;
    private @Nullable EquipmentSlot hand;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @NotNull Entity entity() {
        if (what == null) {
            what = Objects.requireNonNull(Entities.wrapEntity(event.getEntity()));
        }
        return what;
    }

    @Override
    public @NotNull ItemStack item() {
        if (item == null) {
            if (BukkitFeature.PLAYER_SHEAR_ENTITY_EVENT_ITEM_HAND.isSupported()) {
                item = new BukkitItem(event.getItem());
            } else {
                item = new BukkitItem(event.getPlayer().getItemInHand()); // <= 1.15.1: let's assume he used the item in his main hand (deprecated method used for 1.8.8 compat)
            }
        }
        return item;
    }

    @Override
    public @NotNull EquipmentSlot hand() {
        if (hand == null) {
            if (BukkitFeature.PLAYER_SHEAR_ENTITY_EVENT_ITEM_HAND.isSupported()) {
                hand = EquipmentSlot.of(event.getHand());
            } else {
                hand = EquipmentSlot.of("main_hand"); // <= 1.15.1
            }
        }
        return hand;
    }
}
