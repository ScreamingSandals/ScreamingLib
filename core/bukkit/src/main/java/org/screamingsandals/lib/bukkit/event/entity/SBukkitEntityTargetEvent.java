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
import lombok.experimental.Accessors;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityTargetEvent;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityTargetEvent implements SEntityTargetEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityTargetEvent event;

    // Internal cache
    private EntityBasic entity;
    private TargetReason targetReason;

    @Override
    public EntityBasic entity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    @Nullable
    public EntityBasic target() {
        if (event.getTarget() == null) {
            return null;
        }

        return EntityMapper.wrapEntity(event.getTarget()).orElseThrow();
    }

    @Override
    public void target(@Nullable EntityBasic target) {
        event.setTarget(target == null ? null : target.as(Entity.class));
    }

    @Override
    public TargetReason targetReason() {
        if (targetReason == null) {
            targetReason = TargetReason.valueOf(event.getReason().name());
        }
        return targetReason;
    }
}
