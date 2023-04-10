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

package org.screamingsandals.lib.block;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.ItemBlockIdsRemapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.registry.Registry;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.*;

@ProvidedService
@ApiStatus.Internal
public abstract class BlockTypeRegistry extends Registry<BlockType> {
    private final @NotNull Map<@NotNull ResourceLocation, ResourceLocation> aliasMap = new HashMap<>();

    private static @Nullable BlockTypeRegistry registry;
    private static @Nullable BlockType cachedAir;
    
    public BlockTypeRegistry() {
        super(BlockType.class);
        Preconditions.checkArgument(registry == null, "BlockTypeRegistry is already initialized!");
        registry = this;
    }

    public static @NotNull BlockTypeRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "BlockTypeRegistry is not initialized yet!");
    }

    @Override
    protected @Nullable BlockType resolveMapping0(@NotNull Object object) {
        if (object instanceof ConfigurationNode) {
            object = ((ConfigurationNode) object).getString("");
        }

        @Nullable ResourceLocation location;
        @Nullable String blockState = null;
        if (object instanceof ResourceLocation) {
            location = (ResourceLocation) object;
        } else {
            var str = object.toString().trim();
            var index = str.indexOf('[');
            if (index != -1) {
                location = ResourceLocation.ofNullable(str.substring(0, index));
                blockState = str.substring(index);
            } else {
                location = ResourceLocation.ofNullable(str);
            }
        }

        if (location == null) {
            return null;
        }

        var result = this.resolveMappingPlatform(location, blockState);
        if (result != null) {
            return result;
        }

        var alias = this.aliasMap.get(location);
        if (alias != null) {
            return this.resolveMappingPlatform(location, blockState);
        }

        return null;
    }

    public static @NotNull BlockType colorize(@NotNull BlockType holder, @NotNull String color) {
        Preconditions.checkNotNull(registry, "BlockTypeRegistry is not initialized yet!");
        return ItemBlockIdsRemapper.colorableBlocks.entrySet().stream()
                .filter(c -> c.getKey().test(holder))
                .map(Map.Entry::getValue)
                .findFirst()
                .flatMap(fun -> fun.apply(color))
                .orElse(holder);
    }

    public static @NotNull BlockType getCachedAir() {
        if (cachedAir == null) {
            cachedAir = Preconditions.checkNotNull(registry, "BlockTypeRegistry is not initialized yet!").resolveMapping("minecraft:air");
            Preconditions.checkNotNull(cachedAir, "Could not find block type: minecraft:air");
        }
        return cachedAir;
    }

    public void mapAlias(@NotNull String location, @NotNull String alias) {
        this.mapAlias(ResourceLocation.of(location), ResourceLocation.of(alias));
    }

    protected void mapAlias(@NotNull ResourceLocation location, @NotNull ResourceLocation alias) {
        var locT = this.resolveMappingPlatform(location, null);
        var aliasT = this.resolveMappingPlatform(alias, null);

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

    protected abstract @Nullable BlockType resolveMappingPlatform(@NotNull ResourceLocation location, @Nullable String blockState);
}
