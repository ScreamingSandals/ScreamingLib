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

package org.screamingsandals.lib.impl.utils.registry;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.HashMap;
import java.util.Map;

public abstract class SimpleRegistry<T extends RegistryItem> extends Registry<T> {
    private final @NotNull Map<@NotNull ResourceLocation, ResourceLocation> aliasMap = new HashMap<>();

    @ApiStatus.Internal
    public SimpleRegistry(@NotNull Class<T> type) {
        super(type);
    }

    @Override
    @ApiStatus.Internal
    protected final @Nullable T resolveMapping0(@NotNull Object object) {
        if (object instanceof ConfigurationNode) {
            object = ((ConfigurationNode) object).getString("");
        }

        @Nullable ResourceLocation location;
        if (object instanceof ResourceLocation) {
            location = (ResourceLocation) object;
        } else {
            location = ResourceLocation.ofNullable(object.toString().trim());
        }

        if (location == null) {
            return null;
        }

        var result = this.resolveMappingPlatform(location);
        if (result != null) {
            return result;
        }

        var alias = this.aliasMap.get(location);
        if (alias != null) {
            return this.resolveMappingPlatform(location);
        }

        return null;
    }

    @ApiStatus.Internal
    protected void mapAlias(@NotNull String location, @NotNull String alias) {
        this.mapAlias(ResourceLocation.of(location), ResourceLocation.of(alias));
    }

    @ApiStatus.Internal
    protected void mapAlias(@NotNull ResourceLocation location, @NotNull ResourceLocation alias) {
        var locT = this.resolveMappingPlatform(location);
        var aliasT = this.resolveMappingPlatform(alias);

        if (locT == null && aliasT != null) {
            this.aliasMap.put(location, alias);
        } else if (locT != null && aliasT == null) {
            this.aliasMap.put(alias, location);
        } else {
            // Also map alias to alias
            var locA = this.aliasMap.get(location);
            var aliasA = this.aliasMap.get(alias);

            if (locA == null && aliasA != null) {
                this.aliasMap.put(location, aliasA);
            } else if (locA != null && aliasA == null) {
                this.aliasMap.put(alias, locA);
            }
        }
    }

    @ApiStatus.Internal
    protected abstract @Nullable T resolveMappingPlatform(@NotNull ResourceLocation location);
}
