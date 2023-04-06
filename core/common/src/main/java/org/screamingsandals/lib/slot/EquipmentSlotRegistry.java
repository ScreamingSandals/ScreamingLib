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

package org.screamingsandals.lib.slot;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.registry.SimpleRegistry;

@ProvidedService
@ApiStatus.Internal
public abstract class EquipmentSlotRegistry extends SimpleRegistry<EquipmentSlot> {
    private static @Nullable EquipmentSlotRegistry registry;

    public EquipmentSlotRegistry() {
        super(EquipmentSlot.class);
        Preconditions.checkArgument(registry == null, "EquipmentSlotRegistry is already initialized!");
        registry = this;
    }

    public static @NotNull EquipmentSlotRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "EquipmentSlotRegistry is not initialized yet!");
    }

    @OnPostConstruct
    public void legacyMapping() {
        // Vanilla <-> Bukkit
        mapAlias("MAIN_HAND", "HAND");
        mapAlias("OFF_HAND", "OFF_HAND");
        mapAlias("BOOTS", "FEET");
        mapAlias("LEGGINGS", "LEGS");
        mapAlias("CHESTPLATE", "CHEST");
        mapAlias("HELMET", "HEAD");
    }
}
