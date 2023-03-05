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

package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.block.Blocks;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.LivingEntity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.block.BlockDispenseEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.utils.math.Vector3D;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitBlockDispenseEvent implements BlockDispenseEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.block.BlockDispenseEvent event;

    // Internal cache
    private @Nullable Block block;
    private @Nullable LivingEntity receiver;
    private boolean receiverCached;

    @Override
    public @NotNull Block block() {
        if (block == null) {
            block = Blocks.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public @NotNull ItemStack item() {
        return new BukkitItem(event.getItem());
    }

    @Override
    public void item(@NotNull ItemStack item) {
        event.setItem(item.as(org.bukkit.inventory.ItemStack.class));
    }

    @Override
    public @NotNull Vector3D velocity() {
        return new Vector3D(event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ());
    }

    @Override
    public void velocity(@NotNull Vector3D velocity) {
        event.setVelocity(new Vector(velocity.getX(), velocity.getY(), velocity.getZ()));
    }

    @Override
    public @Nullable LivingEntity receiver() {
        if (!receiverCached) {
            if (event instanceof BlockDispenseArmorEvent) {
                receiver = Entities.wrapEntityLiving(((BlockDispenseArmorEvent) event).getTargetEntity());
            }
            receiverCached = true;
        }
        return receiver;
    }
}
