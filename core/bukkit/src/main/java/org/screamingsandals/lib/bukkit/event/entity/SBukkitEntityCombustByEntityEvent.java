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

import lombok.experimental.ExtensionMethod;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityCombustByEntityEvent;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@ExtensionMethod(value = {NullableExtension.class}, suppressBaseMethods = false)
public class SBukkitEntityCombustByEntityEvent extends SBukkitEntityCombustEvent implements SEntityCombustByEntityEvent {
    public SBukkitEntityCombustByEntityEvent(EntityCombustByEntityEvent event) {
        super(event);
    }

    // Internal cache
    private EntityBasic combuster;

    @Override
    public @NotNull EntityBasic combuster() {
        if (combuster == null) {
            combuster = EntityMapper.wrapEntity(((EntityCombustByEntityEvent) event()).getCombuster()).orElseThrow();
        }
        return combuster;
    }
}
