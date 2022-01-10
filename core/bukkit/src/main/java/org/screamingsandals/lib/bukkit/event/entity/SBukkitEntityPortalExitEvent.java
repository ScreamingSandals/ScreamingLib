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
import lombok.experimental.Accessors;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityPortalExitEvent;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityPortalExitEvent implements SEntityPortalExitEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityPortalExitEvent event;

    // Internal cache
    private EntityBasic entity;
    private Vector3D before;

    @Override
    public EntityBasic entity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public LocationHolder from() {
        return LocationMapper.wrapLocation(event.getFrom());
    }

    @Override
    public void from(LocationHolder location) {
        event.setFrom(location.as(Location.class));
    }

    @Override
    public LocationHolder to() {
        return LocationMapper.wrapLocation(event.getTo());
    }

    @Override
    public void to(LocationHolder location) {
        event.setTo(location.as(Location.class));
    }

    @Override
    public Vector3D before() {
        if (before == null) {
            before = new Vector3D(event.getBefore().getX(), event.getBefore().getY(), event.getBefore().getZ());
        }
        return before;
    }

    @Override
    public Vector3D after() {
        return new Vector3D(event.getAfter().getX(), event.getAfter().getY(), event.getAfter().getZ());
    }

    @Override
    public void after(Vector3D after) {
        event.setAfter(new Vector(after.getX(), after.getY(), after.getZ()));
    }
}
