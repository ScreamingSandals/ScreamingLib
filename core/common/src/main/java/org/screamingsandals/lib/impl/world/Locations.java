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

package org.screamingsandals.lib.impl.world;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.block.BlockPlacements;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.world.Location;

import java.util.Objects;

/**
 * Class responsible for converting platform locations to wrappers.
 */
@ProvidedService
@ApiStatus.Internal
public abstract class Locations {
    protected final @NotNull BidirectionalConverter<Location> converter = BidirectionalConverter.<Location>build()
            .registerP2W(Location.class, e -> e)
            .registerW2P(BlockPlacement.class, BlockPlacements::resolve);

    private static @Nullable Locations locations;

    /**
     * Constructs the location mapper.
     */
    public Locations() {
        if (locations != null) {
            throw new UnsupportedOperationException("Locations is already initialized.");
        }
        locations = this;
    }

    /**
     * Resolves the supplied platform location to its {@link Location} wrapper, can be empty.
     *
     * @param obj the platform location
     * @return the location wrapper
     */
    @Contract("null -> null")
    public static @Nullable Location resolve(@Nullable Object obj) {
        if (locations == null) {
            throw new UnsupportedOperationException("Locations is not initialized yet.");
        }
        return locations.converter.convertNullable(obj);
    }

    /**
     * Maps the supplied platform location to its {@link Location} wrapper.
     *
     * @param input the platform location
     * @param <T>   the platform location type
     * @return the location wrapper
     * @throws java.util.NoSuchElementException when the location could not be mapped
     */
    public static <T> @NotNull Location wrapLocation(@NotNull T input) {
        return Objects.requireNonNull(resolve(input));
    }

    /**
     * Converts the location holder to a new type (like a platform location type).
     *
     * @param holder  the location holder to convert
     * @param newType the new type class
     * @param <T>     the new type
     * @return the location holder converted to the supplied type
     * @throws UnsupportedOperationException when the wrapper could not be converted to its new type
     */
    public static <T> T convert(@NotNull Location holder, @NotNull Class<T> newType) {
        if (locations == null) {
            throw new UnsupportedOperationException("Locations is not initialized yet.");
        }
        return locations.converter.convert(holder, newType);
    }
}
