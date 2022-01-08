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

package org.screamingsandals.lib.block;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.Optional;

@AbstractService
@ServiceDependencies(dependsOn = {
        LocationMapper.class,
        BlockTypeMapper.class
})
public abstract class BlockMapper {
    protected BidirectionalConverter<BlockHolder> converter = BidirectionalConverter.<BlockHolder>build()
            .registerP2W(BlockHolder.class, e -> e);

    private static BlockMapper mapping;

    @ApiStatus.Internal
    public BlockMapper() {
        Preconditions.checkArgument(mapping == null, "BlockMapper is already initialized!");
        mapping = this;
    }

    public static Optional<BlockHolder> resolve(Object obj) {
        return Preconditions.checkNotNull(mapping, "BlockMapper is not initialized yet!").converter.convertOptional(obj);
    }

    public static <T> BlockHolder wrapBlock(T block) {
        return Preconditions.checkNotNull(mapping, "BlockMapper is not initialized yet!").converter.convert(block);
    }

    public static <T> T convert(BlockHolder holder, Class<T> newType) {
        return Preconditions.checkNotNull(mapping, "BlockMapper is not initialized yet!").converter.convert(holder, newType);
    }

    public static BlockHolder getBlockAt(LocationHolder location) {
        return Preconditions.checkNotNull(mapping, "BlockMapper is not initialized yet!").getBlockAt0(location);
    }

    public static void setBlockAt(LocationHolder location, BlockTypeHolder material, boolean ignorePhysics) {
        Preconditions.checkNotNull(mapping, "BlockMapper is not initialized yet!").setBlockAt0(location, material, ignorePhysics);
    }

    public static void breakNaturally(LocationHolder location) {
        Preconditions.checkNotNull(mapping, "BlockMapper is not initialized yet!").breakNaturally0(location);
    }

    protected abstract BlockHolder getBlockAt0(LocationHolder location);

    protected abstract void setBlockAt0(LocationHolder location, BlockTypeHolder material, boolean ignorePhysics);

    protected abstract void breakNaturally0(LocationHolder location);
}
