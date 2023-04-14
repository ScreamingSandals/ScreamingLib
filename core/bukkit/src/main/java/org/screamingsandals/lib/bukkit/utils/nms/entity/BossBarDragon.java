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

package org.screamingsandals.lib.bukkit.utils.nms.entity;

import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.EnderDragonAccessor;
import org.screamingsandals.lib.nms.accessors.EntityAccessor;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BossBarDragon extends FakeEntityNMS<EnderDragon> {

    public BossBarDragon(@NotNull Location location) {
        super(construct(location));
        setInvisible(true);
    }

    public static @NotNull Object construct(@NotNull Location location) {
        final Object nmsEntity = Reflect.construct(EnderDragonAccessor.getConstructor0(), ClassStorage.getHandle(location.getWorld()));
        Reflect.fastInvoke(nmsEntity, EntityAccessor.getMethodAbsMoveTo1(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
        return nmsEntity;
    }

    @Override
    public @NotNull Location createPosition(@NotNull Location position) {
        final Location clone = position.clone();
        clone.setPitch(0);
        clone.setYaw(0);
        clone.setY(clone.getY() - 500);
        return clone;
    }
}
