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

import lombok.*;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.bukkit.event.Cancellable;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.bukkit.event.NoAutoCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SVehicleCreateEvent;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class SBukkitVehicleCreateEvent implements SVehicleCreateEvent, NoAutoCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final VehicleCreateEvent event;

    // Internal cache
    private EntityBasic entity;

    @Override
    public @NotNull EntityBasic entity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getVehicle()).orElseThrow();
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
