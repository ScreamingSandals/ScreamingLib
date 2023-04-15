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

package org.screamingsandals.lib.impl.bukkit.attribute;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.impl.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;

import java.util.Arrays;
import java.util.List;

@Service
public class BukkitAttributeTypeRegistry1_16 extends BukkitAttributeTypeRegistry {
    public BukkitAttributeTypeRegistry1_16() {
        specialType(Attribute.class, BukkitAttributeType1_16::new);
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull AttributeType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(Attribute.values()),
                BukkitAttributeType1_16::new,
                attributeType -> {
                    var key = attributeType.getKey();
                    return ResourceLocation.of(key.getNamespace(), key.getKey());
                },
                (attributeType, literal) -> attributeType.getKey().getKey().contains(literal),
                (attributeType, namespace) -> attributeType.getKey().getNamespace().equals(namespace),
                List.of()
        );
    }

    @Override
    protected @Nullable AttributeType resolveMappingPlatform(@NotNull ResourceLocation location) {
        var entityType = Registry.ATTRIBUTE.get(new NamespacedKey(location.namespace(), location.path()));
        if (entityType != null) {
            return new BukkitAttributeType1_16(entityType);
        }
        return null;
    }
}
