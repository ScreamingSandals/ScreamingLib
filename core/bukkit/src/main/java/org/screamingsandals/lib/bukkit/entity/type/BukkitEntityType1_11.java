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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.bukkit.tags.KeyedUtils;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.Location;

import java.util.Arrays;

public class BukkitEntityType1_11 extends BasicWrapper<org.bukkit.entity.EntityType> implements EntityType {
    public BukkitEntityType1_11(@NotNull org.bukkit.entity.EntityType wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean isAlive() {
        return wrappedObject.isAlive();
    }

    @Override
    public boolean hasTag(@NotNull Object tag) {
        ResourceLocation key;
        if (tag instanceof ResourceLocation) {
            key = (ResourceLocation) tag;
        } else {
            key = ResourceLocation.of(tag.toString());
        }
        // native tags (while they have been implemented in 1.14, Bukkit API didn't have them until late build of 1.17.1
        if (Server.isVersion(1, 13)) {
            if (Reflect.getField(Tag.class, "REGISTRY_ENTITY_TYPES") != null) {
                KeyedUtils.isTagged(Tag.REGISTRY_ENTITY_TYPES, new NamespacedKey(key.namespace(), key.path()), org.bukkit.entity.EntityType.class, wrappedObject);
            } else if (Reflect.getField(Tag.class, "REGISTRY_ENTITIES") != null) { // Paper implemented them earlier in 1.16.5
                KeyedUtils.isTagged((String) Reflect.getField(Tag.class, "REGISTRY_ENTITIES"), new NamespacedKey(key.namespace(), key.path()), org.bukkit.entity.EntityType.class, wrappedObject);
            } // TODO: else bypass using NMS on CB-like servers
        }
        // backported tags
        if (!"minecraft".equals(key.namespace())) {
            return false;
        }
        var value = key.path();
        return BukkitEntityTypeRegistry.hasTagInBackPorts(wrappedObject, value);
    }

    @Override
    public boolean is(@Nullable Object entityType) {
        if (entityType instanceof org.bukkit.entity.EntityType || entityType instanceof EntityType) {
            return equals(entityType);
        }
        if (entityType instanceof String) {
            var str = (String) entityType;
            if (str.startsWith("#")) {
                // seems like a tag
                return hasTag(str.substring(1));
            }
        }
        return equals(EntityType.ofNullable(entityType));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... entityTypes) {
        return Arrays.stream(entityTypes).anyMatch(this::is);
    }

    @Override
    public @Nullable Entity spawn(@NotNull Location location) {
        return Entities.spawn(this, location);
    }

    @Override
    public @NotNull ResourceLocation location() {
        if (Server.isVersion(1, 14)) {
            var namespaced = wrappedObject.getKey();
            return ResourceLocation.of(namespaced.namespace(), namespaced.getKey());
        } else {
            return InternalEntityLegacyConstants.translateLegacyName1_11(wrappedObject);
        }
    }
}
