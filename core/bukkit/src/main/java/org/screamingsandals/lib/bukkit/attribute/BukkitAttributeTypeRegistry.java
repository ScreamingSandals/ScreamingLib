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

package org.screamingsandals.lib.bukkit.attribute;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.attribute.AttributeTypeRegistry;;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.utils.registry.SimpleRegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitAttributeTypeRegistry extends AttributeTypeRegistry {


    private static final boolean HAS_ATTRIBUTE = Reflect.has("org.bukkit.attribute.Attribute");

    public BukkitAttributeTypeRegistry() {
        if (HAS_ATTRIBUTE) {
            specialType(Attribute.class, BukkitAttributeType::new);
        } else {
            // TODO: implement for 1.8.8
        }
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull AttributeType> getRegistryItemStream0() {
        if (HAS_ATTRIBUTE) {
            if (Server.isVersion(1, 16)) {
                return new SimpleRegistryItemStream<>(
                        () -> Arrays.stream(Attribute.values()),
                        BukkitAttributeType::new,
                        attributeType -> ResourceLocation.of(attributeType.getKey().getNamespace(), attributeType.getKey().getKey()),
                        (attributeType, literal) -> attributeType.getKey().getKey().contains(literal),
                        (attributeType, namespace) -> attributeType.getKey().getNamespace().equals(namespace),
                        List.of()
                );
            } else {
                return new SimpleRegistryItemStream<>(
                        () -> Arrays.stream(Attribute.values()),
                        BukkitAttributeType::new,
                        BukkitAttributeType::getLocation,
                        (attributeType, literal) -> BukkitAttributeType.getLocation(attributeType).path().contains(literal),
                        (attributeType, namespace) -> "minecraft".equals(namespace),
                        List.of()
                );
            }
        }
        // TODO: implement for 1.8.8
        return SimpleRegistryItemStream.createDummy();
    }

    @Override
    protected @Nullable AttributeType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!HAS_ATTRIBUTE) {
            // TODO: implement for 1.8.8
            return null;
        }

        if (Server.isVersion(1, 16)) {
            var entityType = Registry.ATTRIBUTE.get(new NamespacedKey(location.namespace(), location.path()));
            if (entityType != null) {
                return new BukkitAttributeType(entityType);
            }
        } else {
            if (!"minecraft".equals(location.namespace())) {
                return null;
            }

            try {
                var path = location.path().toUpperCase(Locale.ROOT).replace(".", "_");
                var value = Attribute.valueOf(path);
                return new BukkitAttributeType(value);
            } catch (IllegalArgumentException ignored) {
            }
        }

        return null;
    }
}
