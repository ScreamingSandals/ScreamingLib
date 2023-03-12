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
import org.bukkit.Registry;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.bukkit.tags.KeyedUtils;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.entity.type.EntityTypeRegistry;
import org.screamingsandals.lib.entity.type.EntityTypeTagBackPorts;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.utils.registry.SimpleRegistryItemStream;

import java.util.*;

@Service
public class BukkitEntityTypeMapping extends EntityTypeRegistry {
    private static final @NotNull Map<org.bukkit.entity.@NotNull EntityType, List<String>> tagBackPorts = new HashMap<>();

    public BukkitEntityTypeMapping() {
        specialType(org.bukkit.entity.EntityType.class, BukkitEntityType::new);

        if (Server.isVersion(1, 14)) {
            Arrays.stream(org.bukkit.entity.EntityType.values()).forEach(entityType -> {
                NamespacedKey namespaced = null;
                try {
                    namespaced = entityType.getKey();
                } catch (IllegalArgumentException ignored) { // excuse me Bukkit, wtf?
                }

                /* we are probably not able to backport non-minecraft entity tags */
                if (namespaced != null && NamespacedKey.MINECRAFT.equals(namespaced.getNamespace())) {
                    var backPorts = EntityTypeTagBackPorts.getPortedTags(new BukkitEntityType(entityType), s -> {
                        if (Reflect.getField(Tag.class, "REGISTRY_ENTITY_TYPES") != null) {
                            return KeyedUtils.isTagged(Tag.REGISTRY_ENTITY_TYPES, new NamespacedKey("minecraft", s.toLowerCase(Locale.ROOT)), org.bukkit.entity.EntityType.class, entityType);
                        } else if (Reflect.getField(Tag.class, "REGISTRY_ENTITIES") != null) { // Paper implemented them earlier in 1.16.5
                            return KeyedUtils.isTagged(Tag.REGISTRY_ENTITIES, new NamespacedKey("minecraft", s.toLowerCase(Locale.ROOT)), org.bukkit.entity.EntityType.class, entityType);
                        } // TODO: else bypass using NMS on CB-like servers
                        return false;
                    }, Reflect.getField(Tag.class, "REGISTRY_ENTITY_TYPES") != null || Reflect.getField(Tag.class, "REGISTRY_ENTITIES") != null);
                    if (backPorts != null && !backPorts.isEmpty()) {
                        tagBackPorts.put(entityType, backPorts);
                    }
                }
            });
        }
    }

    @Override
    public void aliasMapping() {
        super.aliasMapping();
        // Tags in older versions should be resolved after all aliases are mapped because the backporting code uses flattening names which may not be used yet
        if (!Server.isVersion(1, 14)) {
            Arrays.stream(org.bukkit.entity.EntityType.values()).forEach(type -> {
                var backPorts = EntityTypeTagBackPorts.getPortedTags(new BukkitEntityType(type), s -> false, false);
                if (backPorts != null && !backPorts.isEmpty()) {
                    tagBackPorts.put(type, backPorts);
                }
            });
        }
    }

    @Override
    protected @Nullable EntityType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (Server.isVersion(1, 14)) {
            var entityType = Registry.ENTITY_TYPE.get(new NamespacedKey(location.namespace(), location.path()));
            if (entityType != null) {
                return new BukkitEntityType(entityType);
            }
        } else {
            // TODO: do this properly
            if (!"minecraft".equals(location.namespace())) {
                return null; // how am I supposed to do this?
            }
            var type = org.bukkit.entity.EntityType.fromName(location.path());

            if (type != null) {
                return new BukkitEntityType(type);
            }
        }
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull EntityType> getRegistryItemStream0() {
        if (Server.isVersion(1, 14)) {
            return new SimpleRegistryItemStream<>(
                    () -> Arrays.stream(org.bukkit.entity.EntityType.values()).filter(e -> e != org.bukkit.entity.EntityType.UNKNOWN),
                    BukkitEntityType::new,
                    entityType -> {
                        var namespaced = entityType.getKey();
                        return ResourceLocation.of(namespaced.getNamespace(), namespaced.getKey());
                    },
                    (entityType, literal) -> entityType.getKey().getKey().contains(literal),
                    (entityType, namespace) -> entityType.getKey().getNamespace().equals(namespace),
                    List.of()
            );
        } else {
            // TODO: make correct translations
            return new SimpleRegistryItemStream<>(
                    () -> Arrays.stream(org.bukkit.entity.EntityType.values()).filter(e -> e != org.bukkit.entity.EntityType.UNKNOWN),
                    BukkitEntityType::new,
                    entityType -> ResourceLocation.of(entityType.name()),
                    (entityType, literal) -> entityType.name().toLowerCase(Locale.ROOT).contains(literal),
                    (entityType, namespace) -> "minecraft".equals(namespace),
                    List.of()
            );
        }
    }

    public static boolean hasTagInBackPorts(org.bukkit.entity.EntityType entityType, String tag) {
        return tagBackPorts.containsKey(entityType) && tagBackPorts.get(entityType).contains(tag);
    }
}
