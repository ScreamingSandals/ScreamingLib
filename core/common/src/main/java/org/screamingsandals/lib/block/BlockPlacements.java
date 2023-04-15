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
import org.screamingsandals.lib.impl.block.BlockRegistry;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.impl.world.Locations;

@ProvidedService
@ServiceDependencies(dependsOn = {
        Locations.class,
        BlockRegistry.class
})
public abstract class BlockPlacements {
    protected final @NotNull BidirectionalConverter<BlockPlacement> converter = BidirectionalConverter.<BlockPlacement>build()
            .registerP2W(BlockPlacement.class, e -> e)
            .registerP2W(Location.class, this::getBlockAt0);

    private static @Nullable BlockPlacements mapping;

    @ApiStatus.Internal
    public BlockPlacements() {
        Preconditions.checkArgument(mapping == null, "BlockPlacements is already initialized!");
        mapping = this;
    }

    @Contract("null -> null")
    public static @Nullable BlockPlacement resolve(@Nullable Object obj) {
        return Preconditions.checkNotNull(mapping, "BlockPlacements is not initialized yet!").converter.convertNullable(obj);
    }

    public static @NotNull BlockPlacement getBlockAt(@NotNull Location location) {
        return Preconditions.checkNotNull(mapping, "BlockPlacements is not initialized yet!").getBlockAt0(location);
    }

    protected abstract @NotNull BlockPlacement getBlockAt0(@NotNull Location location);
}
