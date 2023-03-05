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

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.BasicEntity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.entity.EntityDamageByEntityEvent;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitEntityDamageByEntityEvent extends BukkitEntityDamageEvent implements EntityDamageByEntityEvent {
    public BukkitEntityDamageByEntityEvent(@NotNull org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        super(event);
    }

    // Internal cache
    private @Nullable BasicEntity damager;

    @Override
    public @NotNull BasicEntity damager() {
        if (damager == null) {
            damager = Entities.wrapEntity(((org.bukkit.event.entity.EntityDamageByEntityEvent) event()).getDamager()).orElseThrow();
        }
        return damager;
    }
}
