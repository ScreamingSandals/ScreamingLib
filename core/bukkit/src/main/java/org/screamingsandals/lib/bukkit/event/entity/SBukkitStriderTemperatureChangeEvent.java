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

import lombok.*;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.bukkit.event.entity.StriderTemperatureChangeEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SStriderTemperatureChangeEvent;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = {NullableExtension.class}, suppressBaseMethods = false)
public class SBukkitStriderTemperatureChangeEvent implements SStriderTemperatureChangeEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final StriderTemperatureChangeEvent event;

    // Internal cache
    private EntityLiving entity;

    @Override
    public @NotNull EntityLiving entity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntityLiving(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public boolean shivering() {
        return event.isShivering();
    }
}
