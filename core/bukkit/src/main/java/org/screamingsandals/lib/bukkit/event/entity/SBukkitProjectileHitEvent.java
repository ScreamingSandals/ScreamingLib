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
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SProjectileHitEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = {NullableExtension.class}, suppressBaseMethods = false)
public class SBukkitProjectileHitEvent implements SProjectileHitEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final ProjectileHitEvent event;

    // Internal cache
    private EntityBasic entity;
    private EntityBasic hitEntity;
    private boolean hitEntityCached;
    private BlockHolder hitBlock;
    private boolean hitBlockCached;
    private BlockFace hitFace;
    private boolean hitFaceCached;

    @Override
    public @NotNull EntityBasic entity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public @Nullable EntityBasic hitEntity() {
        if (!hitEntityCached) {
            if (event.getHitEntity() != null) {
                hitEntity = EntityMapper.wrapEntity(event.getHitEntity()).orElseThrow();
            }
            hitEntityCached = true;
        }
        return hitEntity;
    }

    @Override
    public @Nullable BlockHolder hitBlock() {
        if (!hitBlockCached) {
            if (event.getHitBlock() != null) {
                hitBlock = BlockMapper.wrapBlock(event.getHitBlock());
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
