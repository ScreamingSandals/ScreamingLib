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

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.vehicle.Vehicle;
import org.screamingsandals.lib.event.entity.VehicleCreateEvent;

import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitVehicleCreateEvent implements VehicleCreateEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.vehicle.VehicleCreateEvent event;

    // Internal cache
    private @Nullable Vehicle entity;

    @Override
    public @NotNull Vehicle entity() {
        if (entity == null) {
            entity = (Vehicle) Objects.requireNonNull(Entities.wrapEntity(event.getVehicle()));
        }
        return entity;
    }

    @Override
    public boolean cancelled() {
        //noinspection ConstantConditions - older versions of Bukkit API
        if (event instanceof Cancellable) {
            return event.isCancelled();
        }
        return false;
    }

    @Override
    public void cancelled(boolean cancel) {
        //noinspection ConstantConditions - older versions of Bukkit API
        if (event instanceof Cancellable) {
            event.setCancelled(true);
        }
    }
}
