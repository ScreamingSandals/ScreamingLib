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

package org.screamingsandals.lib.impl.bukkit.entity.projectile;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.projectile.AbstractArrow;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;

public class BukkitAbstractArrow extends BukkitProjectileEntity implements AbstractArrow {
    public BukkitAbstractArrow(@NotNull org.bukkit.entity.Projectile wrappedObject) {
        super(wrappedObject);
        if (BukkitFeature.ENTITY_ABSTRACT_ARROW.isSupported()) {
            if (!(wrappedObject instanceof org.bukkit.entity.AbstractArrow)) {
                throw new IllegalArgumentException("Wrapped object is not instance of AbstractArrow!");
            }
        } else {
            if (!(wrappedObject instanceof org.bukkit.entity.Arrow)) {
                throw new IllegalArgumentException("Wrapped object is not instance of AbstractArrow!");
            }
        }
    }
}
