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

package org.screamingsandals.lib.impl.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.entity.EntityShootBowEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.slot.EquipmentSlot;

import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitEntityShootBowEvent implements EntityShootBowEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.entity.EntityShootBowEvent event;

    // Internal cache
    private @Nullable Entity entity;
    private @Nullable ItemStack bow;
    private boolean bowCached;
    private @Nullable ItemStack consumable;
    private boolean consumableCached;
    private @Nullable EquipmentSlot hand;

    @Override
    public @NotNull Entity entity() {
        if (entity == null) {
            entity = Objects.requireNonNull(Entities.wrapEntity(event.getEntity()));
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
            if (BukkitFeature.ENTITY_SHOOT_BOW_EVENT_CONSUMABLE.isSupported()) {
                if (event.getConsumable() != null) {
                    consumable = new BukkitItem(event.getConsumable());
                }
            } else {
                // <= 1.16.1
                consumable = null;
            }
            consumableCached = true;
        }
        return consumable;
    }

    @Override
    public @NotNull Entity projectile() {
        return Objects.requireNonNull(Entities.wrapEntity(event.getProjectile())); // Mutable in Bukkit
    }

    @Override
    public @NotNull EquipmentSlot hand() {
        if (hand == null) {
            if (BukkitFeature.ENTITY_SHOOT_BOW_EVENT_HAND.isSupported()) {
                hand = EquipmentSlot.of(event.getHand());
            } else {
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
        if (BukkitFeature.ENTITY_SHOOT_BOW_EVENT_CONSUMABLE.isSupported()) {
            return event.shouldConsumeItem();
        } else {
            return false; // <= 1.16.1
        }
    }

    @Override
    public void consumeItem(boolean consumeItem) {
        if (BukkitFeature.ENTITY_SHOOT_BOW_EVENT_CONSUMABLE.isSupported()) {
            event.setConsumeItem(consumeItem);
        } else {
            // <= 1.16.1
        }
    }
}
