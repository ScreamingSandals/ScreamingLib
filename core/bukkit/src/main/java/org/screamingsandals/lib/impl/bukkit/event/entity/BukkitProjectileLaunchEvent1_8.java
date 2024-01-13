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
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.projectile.ProjectileEntity;
import org.screamingsandals.lib.entity.projectile.ProjectileShooter;
import org.screamingsandals.lib.event.entity.ProjectileLaunchEvent;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;

import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitProjectileLaunchEvent1_8 implements ProjectileLaunchEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.entity.ProjectileLaunchEvent event;

    // Internal cache
    private @Nullable ProjectileShooter shooter;
    private @Nullable ProjectileEntity entity;

    @Override
    public @NotNull ProjectileEntity entity() {
        if (entity == null) {
            entity = (ProjectileEntity) Objects.requireNonNull(Entities.wrapEntity(event.getEntity()));
        }
        return entity;
    }

    @Override
    public @Nullable ProjectileShooter shooter() {
        if (shooter == null) {
            shooter = entity().getShooter();
        }
        return shooter;
    }
}
