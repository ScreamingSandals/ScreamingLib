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

package org.screamingsandals.lib.entity.type;

import org.jetbrains.annotations.*;
import org.screamingsandals.lib.TaggableHolder;
import org.screamingsandals.lib.api.types.server.EntityTypeHolder;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.impl.entity.type.EntityTypeRegistry;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.world.Location;

import java.util.function.Consumer;

public interface EntityType extends RegistryItem, RawValueHolder, TaggableHolder, EntityTypeHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    boolean isAlive();

    @Override
    boolean hasTag(@MinecraftType(MinecraftType.Type.ENTITY_TYPE_TAG) @NotNull Object tag);

    /**
     * Compares the entity type and the object
     *
     * @param entityType Object that represents entity type
     * @return true if specified entity type is the same as this
     */
    @Override
    boolean is(@MinecraftType(MinecraftType.Type.ENTITY_TYPE_OR_TAG) @Nullable Object entityType);

    /**
     * Compares the entity type and the objects
     *
     * @param entityTypes Array of objects that represents entity type
     * @return true if at least one of the entity type objects is same as this
     */
    @Override
    boolean is(@MinecraftType(MinecraftType.Type.ENTITY_TYPE_OR_TAG) @Nullable Object @NotNull... entityTypes);

    default @Nullable Entity spawn(@NotNull Location location) {
        return spawn(location, null);
    }

    default @Nullable Entity spawn(@NotNull Location location, @Nullable Consumer<? super @NotNull Entity> preSpawnFunction) {
        return Entities.spawn(this, location, preSpawnFunction);
    }

    static @NotNull EntityType of(@MinecraftType(MinecraftType.Type.ENTITY_TYPE) @NotNull Object entityType) {
        var result = ofNullable(entityType);
        Preconditions.checkNotNullIllegal(result, "Could not find entity type: " + entityType);
        return result;
    }

    @Contract("null -> null")
    static @Nullable EntityType ofNullable(@MinecraftType(MinecraftType.Type.ENTITY_TYPE) @Nullable Object entityType) {
        if (entityType instanceof EntityType) {
            return (EntityType) entityType;
        }
        return EntityTypeRegistry.getInstance().resolveMapping(entityType);
    }

    static @NotNull RegistryItemStream<@NotNull EntityType> all() {
        return EntityTypeRegistry.getInstance().getRegistryItemStream();
    }
}
