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

package org.screamingsandals.lib.entity.type;

import org.jetbrains.annotations.*;
import org.screamingsandals.lib.TaggableHolder;
import org.screamingsandals.lib.entity.BasicEntity;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.world.Location;

public interface EntityType extends RegistryItem, RawValueHolder, TaggableHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    boolean isAlive();

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE_TAG)
    boolean hasTag(@NotNull Object tag);

    /**
     * Compares the entity type and the object
     *
     * @param entityType Object that represents entity type
     * @return true if specified entity type is the same as this
     */
    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE)
    boolean is(@Nullable Object entityType);

    /**
     * Compares the entity type and the objects
     *
     * @param entityTypes Array of objects that represents entity type
     * @return true if at least one of the entity type objects is same as this
     */
    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE)
    boolean is(@Nullable Object @NotNull... entityTypes);

    @Nullable BasicEntity spawn(@NotNull Location location);

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE)
    static @NotNull EntityType of(@NotNull Object entityType) {
        var result = ofNullable(entityType);
        Preconditions.checkNotNullIllegal(result, "Could not find entity type: " + entityType);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE)
    @Contract("null -> null")
    static @Nullable EntityType ofNullable(@Nullable Object entityType) {
        if (entityType instanceof EntityType) {
            return (EntityType) entityType;
        }
        return EntityTypeRegistry.getInstance().resolveMapping(entityType);
    }

    static @NotNull RegistryItemStream<@NotNull EntityType> all() {
        return EntityTypeRegistry.getInstance().getRegistryItemStream();
    }
}
