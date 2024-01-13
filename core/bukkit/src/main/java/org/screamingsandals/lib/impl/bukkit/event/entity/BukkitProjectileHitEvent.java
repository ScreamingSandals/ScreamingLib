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

import lombok.*;
import lombok.experimental.Accessors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockPlacement;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.entity.ProjectileHitEvent;
import org.screamingsandals.lib.utils.BlockFace;

import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitProjectileHitEvent implements ProjectileHitEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.entity.ProjectileHitEvent event;

    // Internal cache
    private @Nullable Entity entity;
    private @Nullable Entity hitEntity;
    private boolean hitEntityCached;
    private @Nullable BlockPlacement hitBlock;
    private boolean hitBlockCached;
    private @Nullable BlockFace hitFace;
    private boolean hitFaceCached;

    @Override
    public @NotNull Entity entity() {
        if (entity == null) {
            entity = Objects.requireNonNull(Entities.wrapEntity(event.getEntity()));
        }
        return entity;
    }

    @Override
    public @Nullable Entity hitEntity() {
        if (!hitEntityCached) {
            if (event.getHitEntity() != null) {
                hitEntity = Objects.requireNonNull(Entities.wrapEntity(event.getHitEntity()));
            }
            hitEntityCached = true;
        }
        return hitEntity;
    }

    @Override
    public @Nullable BlockPlacement hitBlock() {
        if (!hitBlockCached) {
            if (event.getHitBlock() != null) {
                hitBlock = new BukkitBlockPlacement(event.getHitBlock());
            }
            hitBlockCached = true;
        }
        return hitBlock;
    }

    @Override
    public @Nullable BlockFace hitFace() {
        if (!hitFaceCached) {
            if (event.getHitBlockFace() != null) {
                hitFace = BlockFace.valueOf(event.getHitBlockFace().name());
            }
            hitFaceCached = true;
        }
        return hitFace;
    }
}
