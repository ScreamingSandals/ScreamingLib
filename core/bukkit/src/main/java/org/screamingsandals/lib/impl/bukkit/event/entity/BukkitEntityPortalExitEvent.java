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

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.entity.EntityPortalExitEvent;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.impl.world.Locations;

import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitEntityPortalExitEvent implements EntityPortalExitEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.entity.EntityPortalExitEvent event;

    // Internal cache
    private @Nullable Entity entity;
    private @Nullable Vector3D before;

    @Override
    public @NotNull Entity entity() {
        if (entity == null) {
            entity = Objects.requireNonNull(Entities.wrapEntity(event.getEntity()));
        }
        return entity;
    }

    @Override
    public @NotNull Location from() {
        return Locations.wrapLocation(event.getFrom());
    }

    @Override
    public void from(@NotNull Location location) {
        event.setFrom(location.as(org.bukkit.Location.class));
    }

    @Override
    public @Nullable Location to() {
        var loc = event.getTo();
        return loc != null ? Locations.wrapLocation(loc) : null;
    }

    @Override
    public void to(@Nullable Location location) {
        event.setTo(location != null ? location.as(org.bukkit.Location.class) : null);
    }

    @Override
    public @NotNull Vector3D before() {
        if (before == null) {
            before = new Vector3D(event.getBefore().getX(), event.getBefore().getY(), event.getBefore().getZ());
        }
        return before;
    }

    @Override
    public @NotNull Vector3D after() {
        return new Vector3D(event.getAfter().getX(), event.getAfter().getY(), event.getAfter().getZ());
    }

    @Override
    public void after(@NotNull Vector3D after) {
        event.setAfter(new Vector(after.getX(), after.getY(), after.getZ()));
    }
}
