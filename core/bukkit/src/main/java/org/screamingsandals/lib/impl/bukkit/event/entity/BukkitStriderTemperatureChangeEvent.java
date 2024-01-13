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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.animal.Strider;
import org.screamingsandals.lib.event.entity.StriderTemperatureChangeEvent;

import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitStriderTemperatureChangeEvent implements StriderTemperatureChangeEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.entity.StriderTemperatureChangeEvent event;

    // Internal cache
    private @Nullable Strider entity;

    @Override
    public @NotNull Strider entity() {
        if (entity == null) {
            entity = (Strider) Objects.requireNonNull(Entities.wrapEntityLiving(event.getEntity()));
        }
        return entity;
    }

    @Override
    public boolean shivering() {
        return event.isShivering();
    }
}
