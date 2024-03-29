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

package org.screamingsandals.lib.impl.bukkit.entity.type;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.entity.type.EntityTypeRegistry;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.ServiceInitializer;

import java.util.*;

@Service
public abstract class BukkitEntityTypeRegistry extends EntityTypeRegistry {
    protected static final @NotNull Map<org.bukkit.entity.@NotNull EntityType, List<String>> tagBackPorts = new HashMap<>();

    @ServiceInitializer
    public static @NotNull BukkitEntityTypeRegistry init() {
        if (BukkitFeature.ENTITIES_IN_REGISTRY.isSupported()) {
            return new BukkitEntityTypeRegistry1_14();
        } else if (BukkitFeature.NORMAL_ENTITY_RESOURCE_LOCATIONS.isSupported()) {
            return new BukkitEntityTypeRegistry1_11();
        } else {
            return new BukkitEntityTypeRegistry1_8();
        }
    }

    public static boolean hasTagInBackPorts(org.bukkit.entity.@NotNull EntityType entityType, @NotNull String tag) {
        return tagBackPorts.containsKey(entityType) && tagBackPorts.get(entityType).contains(tag);
    }
}
