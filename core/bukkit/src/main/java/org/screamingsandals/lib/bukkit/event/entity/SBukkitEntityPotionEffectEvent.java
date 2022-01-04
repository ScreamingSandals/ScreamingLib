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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityPotionEffectEvent;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityPotionEffectEvent implements SEntityPotionEffectEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityPotionEffectEvent event;

    // Internal cache
    private EntityBasic entity;
    private PotionEffectHolder oldEffect;
    private boolean oldEffectCached;
    private PotionEffectHolder newEffect;
    private boolean newEffectCached;
    private Cause cause;
    private Action action;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    @Nullable
    public PotionEffectHolder getOldEffect() {
        if (!oldEffectCached) {
            if (event.getOldEffect() != null) {
                oldEffect = PotionEffectHolder.of(event.getOldEffect());
            }
            oldEffectCached = true;
        }
        return oldEffect;
    }

    @Override
    @Nullable
    public PotionEffectHolder getNewEffect() {
        if (!newEffectCached) {
            if (event.getOldEffect() != null) {
                newEffect = PotionEffectHolder.of(event.getNewEffect());
            }
            newEffectCached = true;
        }
        return newEffect;
    }

    @Override
    public Cause getCause() {
        if (cause == null) {
            cause = Cause.valueOf(event.getCause().name());
        }
        return cause;
    }

    @Override
    public Action getAction() {
        if (action == null) {
            action = Action.valueOf(event.getAction().name());
        }
        return action;
    }

    @Override
    public boolean isOverride() {
        return event.isOverride();
    }

    @Override
    public void setOverride(boolean override) {
        event.setOverride(override);
    }
}
