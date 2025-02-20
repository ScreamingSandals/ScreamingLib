/*
 * Copyright 2025 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.entity.type;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.utils.annotations.ServiceFactory;

@UtilityClass
@ServiceFactory
public class BukkitEntityTypeServiceFactory {
    public static @NotNull BukkitEntityTypeRegistry create() {
        if (BukkitFeature.ENTITIES_IN_REGISTRY.isSupported()) {
            return new BukkitEntityTypeRegistry1_14();
        } else if (BukkitFeature.NORMAL_ENTITY_RESOURCE_LOCATIONS.isSupported()) {
            return new BukkitEntityTypeRegistry1_11();
        } else {
            return new BukkitEntityTypeRegistry1_8();
        }
    }
}
