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

package org.screamingsandals.lib.impl.entity.type;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.types.server.EntityTypeHolder;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistry;

import java.util.Objects;

@ProvidedService
@ApiStatus.Internal
public abstract class EntityTypeRegistry extends SimpleRegistry<EntityType> {
    static {
        EntityTypeHolder.Provider.registerProvider(o ->
                Objects.requireNonNull(getInstance().resolveMapping(o), "Could not wrap " + o + " to EntityType")
        );
    }

    private static @Nullable EntityTypeRegistry registry;

    public EntityTypeRegistry() {
        super(EntityType.class);
        Preconditions.checkArgument(registry == null, "EntityTypeMapping is already initialized!");
        registry = this;
    }

    public static @NotNull EntityTypeRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "EntityTypeRegistry is not initialized yet!");
    }

    @OnPostConstruct
    public void aliasMapping() {
        // Newer flattening <-> Older flattening
        mapAlias("ZOMBIFIED_PIGLIN", "ZOMBIE_PIGMAN");
    }
}
