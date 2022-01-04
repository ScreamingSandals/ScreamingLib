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

package org.screamingsandals.lib.bukkit.event.entity;

import lombok.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SProjectileHitEvent;
import org.screamingsandals.lib.utils.BlockFace;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
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
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    @Nullable
    public EntityBasic getHitEntity() {
        if (!hitEntityCached) {
            if (event.getHitEntity() != null) {
                hitEntity = EntityMapper.wrapEntity(event.getHitEntity()).orElseThrow();
            }
            hitEntityCached = true;
        }
        return hitEntity;
    }

    @Override
    @Nullable
    public BlockHolder getHitBlock() {
        if (!hitBlockCached) {
            if (event.getHitBlock() != null) {
                hitBlock = BlockMapper.wrapBlock(event.getHitBlock());
            }
            hitBlockCached = true;
        }
        return hitBlock;
    }

    @Override
    @Nullable
    public BlockFace getHitFace() {
        if (!hitFaceCached) {
            if (event.getHitBlockFace() != null) {
                hitFace = BlockFace.valueOf(event.getHitBlockFace().name());
            }
            hitFaceCached = true;
        }
        return hitFace;
    }
}
