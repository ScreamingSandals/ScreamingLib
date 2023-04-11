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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.entity.EntityPotionEffectEvent;
import org.screamingsandals.lib.item.meta.PotionEffect;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitEntityPotionEffectEvent implements EntityPotionEffectEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.entity.EntityPotionEffectEvent event;

    // Internal cache
    private @Nullable Entity entity;
    private @Nullable PotionEffect oldEffect;
    private boolean oldEffectCached;
    private @Nullable PotionEffect newEffect;
    private boolean newEffectCached;
    private @Nullable Cause cause;
    private @Nullable Action action;

    @Override
    public @NotNull Entity entity() {
        if (entity == null) {
            entity = Entities.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public @Nullable PotionEffect oldEffect() {
        if (!oldEffectCached) {
            if (event.getOldEffect() != null) {
                oldEffect = PotionEffect.of(event.getOldEffect());
            }
            oldEffectCached = true;
        }
        return oldEffect;
    }

    @Override
    public @Nullable PotionEffect newEffect() {
        if (!newEffectCached) {
            if (event.getNewEffect() != null) {
                newEffect = PotionEffect.of(event.getNewEffect());
            }
            newEffectCached = true;
        }
        return newEffect;
    }

    @Override
    public @NotNull Cause cause() {
        if (cause == null) {
            cause = Cause.valueOf(event.getCause().name());
        }
        return cause;
    }

    @Override
    public @NotNull Action action() {
        if (action == null) {
            action = Action.valueOf(event.getAction().name());
        }
        return action;
    }

    @Override
    public boolean override() {
        return event.isOverride();
    }

    @Override
    public void override(boolean override) {
        event.setOverride(override);
    }
}
