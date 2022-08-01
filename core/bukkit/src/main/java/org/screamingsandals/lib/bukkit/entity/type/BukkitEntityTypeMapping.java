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

package org.screamingsandals.lib.bukkit.entity.type;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitEntityTypeMapping extends EntityTypeMapping {
    public BukkitEntityTypeMapping() {
        entityTypeConverter
                .registerP2W(EntityType.class, BukkitEntityTypeHolder::new);

        if (Server.isVersion(1, 14)) {
            Arrays.stream(EntityType.values()).forEach(entityType -> {
                var holder = new BukkitEntityTypeHolder(entityType);
                var namespaced = entityType.getKey();
                /* In case this is a hybrid server and it actually works correctly (unlike Mohist), we should not assume everything is in minecraft namespace */
                mapping.put(NamespacedMappingKey.of(namespaced.getNamespace(), namespaced.getKey()), holder);
                if (NamespacedKey.MINECRAFT.equals(namespaced.namespace()) && !entityType.name().equalsIgnoreCase(namespaced.getKey())) {
                    // Bukkit API is sus
                    mapping.put(NamespacedMappingKey.of(entityType.name()), holder);
                }
                values.add(holder);
            });
        } else {
            Arrays.stream(EntityType.values()).forEach(entityType -> {
                var holder = new BukkitEntityTypeHolder(entityType);
                /* In legacy and 1.13 bukkit api we are not able to determine the namespace */
                mapping.put(NamespacedMappingKey.of(entityType.name()), holder);
                values.add(holder);
            });
        }
    }
}
