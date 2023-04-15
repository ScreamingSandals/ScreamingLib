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

package org.screamingsandals.lib.impl.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.LightningStrike;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.entity.CreeperPowerEvent;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitCreeperPowerEvent implements CreeperPowerEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.entity.CreeperPowerEvent event;

    // Internal cache
    private @Nullable Entity entity;
    private @Nullable LightningStrike bolt;
    private boolean boltCached;
    private @Nullable PowerCause cause;

    @Override
    public @NotNull Entity entity() {
        if (entity == null) {
            entity = Entities.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public @Nullable LightningStrike bolt() {
        if (!boltCached) {
            if (event.getLightning() != null) {
                bolt = Entities.wrapEntityLightning(event.getLightning()).orElseThrow();
            }
            boltCached = true;
        }
        return bolt;
    }

    @Override
    public @NotNull PowerCause cause() {
        if (cause == null) {
            cause = PowerCause.valueOf(event.getCause().name());
        }
        return cause;
    }
}