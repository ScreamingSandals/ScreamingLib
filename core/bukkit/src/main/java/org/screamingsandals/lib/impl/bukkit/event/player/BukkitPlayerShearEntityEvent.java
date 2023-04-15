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

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.player.PlayerShearEntityEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
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
            what = Entities.wrapEntity(event.getEntity()).orElseThrow();
        }
        return what;
    }

    @Override
    public @NotNull ItemStack item() {
        if (item == null) {
            try {
                item = new BukkitItem(event.getItem());
            } catch (Throwable ignored) {
                item = new BukkitItem(event.getPlayer().getItemInHand()); // <= 1.15.1: let's assume he used the item in his main hand (deprecated method used for 1.8.8 compat)
            }
        }
        return item;
    }

    @Override
    public @NotNull EquipmentSlot hand() {
        if (hand == null) {
            try {
                hand = EquipmentSlot.of(event.getHand());
            } catch (Throwable ignored) {
                hand = EquipmentSlot.of("main_hand"); // <= 1.15.1
            }
        }
        return hand;
    }
}