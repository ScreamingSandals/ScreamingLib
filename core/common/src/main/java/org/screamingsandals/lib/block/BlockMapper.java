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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

@AbstractService
@ServiceDependencies(dependsOn = {
        LocationMapper.class,
        BlockTypeMapper.class
})
public abstract class BlockMapper {
    protected final @NotNull BidirectionalConverter<BlockHolder> converter = BidirectionalConverter.<BlockHolder>build()
            .registerP2W(BlockHolder.class, e -> e);

    private static @Nullable BlockMapper mapping;

    @ApiStatus.Internal
    public BlockMapper() {
        Preconditions.checkArgument(mapping == null, "BlockMapper is already initialized!");
        mapping = this;
    }

    @Contract("null -> null")
    public static @Nullable BlockHolder resolve(@Nullable Object obj) {
        return Preconditions.checkNotNull(mapping, "BlockMapper is not initialized yet!").converter.convertNullable(obj);
    }

    public static <T> BlockHolder wrapBlock(@NotNull T block) {
        return Preconditions.checkNotNull(mapping, "BlockMapper is not initialized yet!").converter.convert(block);
    }

    public static <T> T convert(@NotNull BlockHolder holder, @NotNull Class<T> newType) {
        return Preconditions.checkNotNull(mapping, "BlockMapper is not initialized yet!").converter.convert(holder, newType);
    }

    public static @NotNull BlockHolder getBlockAt(@NotNull LocationHolder location) {
        return Preconditions.checkNotNull(mapping, "BlockMapper is not initialized yet!").getBlockAt0(location);
    }

    public static void setBlockAt(@NotNull LocationHolder location, @NotNull BlockTypeHolder material, boolean ignorePhysics) {
        Preconditions.checkNotNull(mapping, "BlockMapper is not initialized yet!").setBlockAt0(location, material, ignorePhysics);
    }

    public static void breakNaturally(@NotNull LocationHolder location) {
        Preconditions.checkNotNull(mapping, "BlockMapper is not initialized yet!").breakNaturally0(location);
    }

    protected abstract @NotNull BlockHolder getBlockAt0(@NotNull LocationHolder location);

    protected abstract void setBlockAt0(@NotNull LocationHolder location, @NotNull BlockTypeHolder material, boolean ignorePhysics);

    protected abstract void breakNaturally0(@NotNull LocationHolder location);
}
