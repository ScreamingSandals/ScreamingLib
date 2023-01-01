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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityDamageByEntityEvent;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@ExtensionMethod(value = {NullableExtension.class}, suppressBaseMethods = false)
public class SBukkitEntityDamageByEntityEvent extends SBukkitEntityDamageEvent implements SEntityDamageByEntityEvent {
    public SBukkitEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        super(event);
    }

    // Internal cache
    private EntityBasic damager;

    @Override
    public @NotNull EntityBasic damager() {
        if (damager == null) {
            damager = EntityMapper.wrapEntity(((EntityDamageByEntityEvent) event()).getDamager()).orElseThrow();
        }
        return damager;
    }
}
