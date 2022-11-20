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

package org.screamingsandals.lib.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.math.Vector3D;

public interface ProjectileShooter extends Wrapper {
    default @Nullable EntityProjectile launchProjectile(@NotNull Object projectileType) {
        if (projectileType instanceof EntityTypeHolder) {
            return launchProjectile(projectileType);
        } else {
            var projectile = EntityTypeHolder.ofNullable(projectileType);
            if (projectile != null) {
                return this.launchProjectile(projectile);
            }
            return null;
        }
    }

    @Nullable EntityProjectile launchProjectile(@NotNull EntityTypeHolder projectileType);

    default @Nullable EntityProjectile launchProjectile(@NotNull Object projectileType, @NotNull Vector3D velocity) {
        if (projectileType instanceof EntityTypeHolder) {
            return launchProjectile(projectileType, velocity);
        } else {
            var projectile = EntityTypeHolder.ofNullable(projectileType);
            if (projectile != null) {
                return this.launchProjectile(projectile, velocity);
            }
            return null;
        }
    }

    @Nullable EntityProjectile launchProjectile(@NotNull EntityTypeHolder projectileType, @NotNull Vector3D velocity);
}
