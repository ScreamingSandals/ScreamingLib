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

package org.screamingsandals.lib.bukkit.event.entity;

import lombok.*;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.BasicEntity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.entity.EntityShootBowEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitEntityShootBowEvent implements EntityShootBowEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.entity.EntityShootBowEvent event;

    // Internal cache
    private @Nullable BasicEntity entity;
    private @Nullable ItemStack bow;
    private boolean bowCached;
    private @Nullable ItemStack consumable;
    private boolean consumableCached;
    private @Nullable EquipmentSlot hand;

    @Override
    public @NotNull BasicEntity entity() {
        if (entity == null) {
            entity = Entities.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public @Nullable ItemStack bow() {
        if (!bowCached) {
            if (event.getBow() != null) {
                bow = new BukkitItem(event.getBow());
            }
            bowCached = true;
        }
        return bow;
    }

    @Override
    public @Nullable ItemStack consumable() {
        if (!consumableCached) {
            try {
                if (event.getConsumable() != null) {
                    consumable = new BukkitItem(event.getConsumable());
                }
                consumableCached = true;
            } catch (Throwable ignored) {
                // <= 1.16.1
                consumable = null;
                consumableCached = true;
            }
        }
        return consumable;
    }

    @Override
    public @NotNull BasicEntity projectile() {
        return Entities.wrapEntity(event.getProjectile()).orElseThrow(); // Mutable in Bukkit
    }

    @Override
    public @NotNull EquipmentSlot hand() {
        if (hand == null) {
            try {
                hand = EquipmentSlot.of(event.getHand());
            } catch (Throwable ignored) {
                hand = EquipmentSlot.of("main_hand"); // event#getHand added to the event in 1.16.2
            }
        }
        return hand;
    }

    @Override
    public float force() {
        return event.getForce();
    }

    @Override
    public boolean consumeItem() {
        try {
            return event.shouldConsumeItem();
        } catch (Throwable ignored) {
            return false; // <= 1.16.1
        }
    }

    @Override
    public void consumeItem(boolean consumeItem) {
        try {
            event.setConsumeItem(consumeItem);
        } catch (Throwable ignored) {
            // <= 1.16.1
        }
    }
}
