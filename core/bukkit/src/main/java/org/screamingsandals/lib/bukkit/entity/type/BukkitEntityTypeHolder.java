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
import org.bukkit.Tag;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.bukkit.tags.KeyedUtils;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Arrays;
import java.util.Optional;

public class BukkitEntityTypeHolder extends BasicWrapper<EntityType> implements EntityTypeHolder {

    public BukkitEntityTypeHolder(EntityType wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean isAlive() {
        return wrappedObject.isAlive();
    }

    @Override
    public boolean hasTag(@NotNull Object tag) {
        NamespacedMappingKey key;
        if (tag instanceof NamespacedMappingKey) {
            key = (NamespacedMappingKey) tag;
        } else {
            key = NamespacedMappingKey.of(tag.toString());
        }
        // native tags (while they have been implemented in 1.14, Bukkit API didn't have them until late build of 1.17.1
        if (Server.isVersion(1, 13)) {
            if (Reflect.getField(Tag.class, "REGISTRY_ENTITY_TYPES") != null) {
                KeyedUtils.isTagged(Tag.REGISTRY_ENTITY_TYPES, new NamespacedKey(key.namespace(), key.value()), EntityType.class, wrappedObject);
            } else if (Reflect.getField(Tag.class, "REGISTRY_ENTITIES") != null) { // Paper implemented them earlier in 1.16.5
                KeyedUtils.isTagged(Tag.REGISTRY_ENTITIES, new NamespacedKey(key.namespace(), key.value()), EntityType.class, wrappedObject);
            } // TODO: else bypass using NMS on CB-like servers
        }
        // backported tags
        if (!key.namespace().equals("minecraft")) {
            return false;
        }
        var value = key.value();
        return BukkitEntityTypeMapping.hasTagInBackPorts(wrappedObject, value);
    }

    @Override
    public boolean is(Object entityType) {
        if (entityType instanceof EntityType || entityType instanceof EntityTypeHolder) {
            return equals(entityType);
        }
        if (entityType instanceof String) {
            var str = (String) entityType;
            if (str.startsWith("#")) {
                // seems like a tag
                return hasTag(str.substring(1));
            }
        }
        return equals(EntityTypeHolder.ofOptional(entityType).orElse(null));
    }

    @Override
    public boolean is(Object... entityTypes) {
        return Arrays.stream(entityTypes).anyMatch(this::is);
    }

    @Override
    public <T extends EntityBasic> Optional<T> spawn(LocationHolder location) {
        return EntityMapper.spawn(this, location);
    }
}
