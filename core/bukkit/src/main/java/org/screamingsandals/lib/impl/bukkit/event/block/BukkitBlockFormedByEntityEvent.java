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

package org.screamingsandals.lib.impl.bukkit.event.block;

import lombok.experimental.ExtensionMethod;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.block.BlockFormedByEntityEvent;

import lombok.experimental.Accessors;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@Accessors(fluent = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitBlockFormedByEntityEvent extends BukkitBlockFormEvent implements BlockFormedByEntityEvent {
    // Internal cache
    private Entity producer;

    public BukkitBlockFormedByEntityEvent(EntityBlockFormEvent event) {
        super(event);
    }

    @Override
    public @NotNull Entity producer() {
        if (producer == null) {
            producer = Entities.wrapEntity(((EntityBlockFormEvent) event()).getEntity()).orElseThrow();
        }
        return producer;
    }
}