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

package org.screamingsandals.lib.bukkit.entity.type;

import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.entity.EntityType;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.bukkit.tags.KeyedUtils;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.entity.type.EntityTypeTagBackPorts;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.*;

@Service
public class BukkitEntityTypeMapping extends EntityTypeMapping {
    private static final Map<EntityType, List<String>> tagBackPorts = new HashMap<>();

    public BukkitEntityTypeMapping() {
        entityTypeConverter
                .registerP2W(EntityType.class, BukkitEntityTypeHolder::new);

        if (Server.isVersion(1, 14)) {
            Arrays.stream(EntityType.values()).forEach(entityType -> {
                var holder = new BukkitEntityTypeHolder(entityType);
                /* In case this is a hybrid server and it actually works correctly (unlike Mohist), we should not assume everything is in minecraft namespace */
                NamespacedKey namespaced = null;
                try {
                    namespaced = entityType.getKey();

                    mapping.put(NamespacedMappingKey.of(namespaced.getNamespace(), namespaced.getKey()), holder);
                    if (NamespacedKey.MINECRAFT.equals(namespaced.getNamespace()) && !entityType.name().equalsIgnoreCase(namespaced.getKey())) {
                        // Bukkit API is sus
                        mapping.put(NamespacedMappingKey.of(entityType.name()), holder);
                    }
                } catch (IllegalArgumentException ignored) { // excuse me Bukkit, wtf?
                    mapping.put(NamespacedMappingKey.of(entityType.name()), holder);
                }
                values.add(holder);
                /* we are probably not able to backport non-minecraft entity tags */
                if (namespaced != null && NamespacedKey.MINECRAFT.equals(namespaced.getNamespace())) {
                    var backPorts = EntityTypeTagBackPorts.getPortedTags(holder, s -> {
                        if (Reflect.getField(Tag.class, "REGISTRY_ENTITY_TYPES") != null) {
                            return KeyedUtils.isTagged(Tag.REGISTRY_ENTITY_TYPES, new NamespacedKey("minecraft", s.toLowerCase(Locale.ROOT)), EntityType.class, entityType);
                        } else if (Reflect.getField(Tag.class, "REGISTRY_ENTITIES") != null) { // Paper implemented them earlier in 1.16.5
                            return KeyedUtils.isTagged(Tag.REGISTRY_ENTITIES, new NamespacedKey("minecraft", s.toLowerCase(Locale.ROOT)), EntityType.class, entityType);
                        } // TODO: else bypass using NMS on CB-like servers
                        return false;
                    }, Reflect.getField(Tag.class, "REGISTRY_ENTITY_TYPES") != null || Reflect.getField(Tag.class, "REGISTRY_ENTITIES") != null);
                    if (backPorts != null && !backPorts.isEmpty()) {
                        tagBackPorts.put(entityType, backPorts);
                    }
                }
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

    @Override
    public void aliasMapping() {
        super.aliasMapping();
        // Tags in older versions should be resolved after all aliases are mapped because the backporting code uses flattening names which may not be used yet
        if (!Server.isVersion(1, 14)) {
            values.forEach(holder -> {
                var backPorts = EntityTypeTagBackPorts.getPortedTags(holder, s -> false, false);
                if (backPorts != null && !backPorts.isEmpty()) {
                    tagBackPorts.put(holder.as(EntityType.class), backPorts);
                }
            });
        }
    }

    public static boolean hasTagInBackPorts(EntityType entityType, String tag) {
        return tagBackPorts.containsKey(entityType) && tagBackPorts.get(entityType).contains(tag);
    }
}
