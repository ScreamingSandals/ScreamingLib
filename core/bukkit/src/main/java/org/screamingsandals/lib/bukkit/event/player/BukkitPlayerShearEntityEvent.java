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

import lombok.*;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.BasicEntity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.player.PlayerShearEntityEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
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
    private @Nullable BasicEntity what;
    private @Nullable ItemStack item;
    private @Nullable EquipmentSlotHolder hand;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @NotNull BasicEntity entity() {
        if (what == null) {
            what = Entities.wrapEntity(event.getEntity()).orElseThrow();
        }
        return what;
    }

    @Override
    public @NotNull ItemStack item() {
        if (item == null) {
            item = new BukkitItem(event.getItem());
        }
        return item;
    }

    @Override
    public @NotNull EquipmentSlotHolder hand() {
        if (hand == null) {
            hand = EquipmentSlotHolder.of(event.getHand());
        }
        return hand;
    }
}
