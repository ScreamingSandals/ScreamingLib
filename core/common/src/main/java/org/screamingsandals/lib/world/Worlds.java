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

package org.screamingsandals.lib.world;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

import java.util.UUID;

/**
 * Class responsible for converting platform worlds to wrappers.
 */
@ProvidedService
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public abstract class Worlds {
    protected final @NotNull BidirectionalConverter<World> converter = BidirectionalConverter.<World>build()
            .registerP2W(World.class, e -> e);

    private static @Nullable Worlds worlds;

    /**
     * Constructs the location mapper.
     */
    @ApiStatus.Internal
    public Worlds() {
        if (worlds != null) {
            throw new UnsupportedOperationException("Worlds is already initialized.");
        }
        worlds = this;
    }

    /**
     * Resolves the supplied platform world to its {@link World} wrapper, can be empty.
     *
     * @param obj the platform world
     * @return the world wrapper
     */
    @Contract("null -> null")
    public static @Nullable World resolve(@Nullable Object obj) {
        if (worlds == null) {
            throw new UnsupportedOperationException("Worlds is not initialized yet.");
        }
        return worlds.converter.convertNullable(obj);
    }

    /**
     * Maps the supplied platform world to its {@link World} wrapper.
     *
     * @param input the platform world
     * @param <T> the platform world type
     * @return the world wrapper
     * @throws java.util.NoSuchElementException when the world could not be mapped
     */
    public static <T> @NotNull World wrapWorld(@NotNull T input) {
        return resolve(input).orElseThrow();
    }

    /**
     * Converts the world holder to a new type (like a platform world type).
     *
     * @param holder the world holder to convert
     * @param newType the new type class
     * @param <T> the new type
     * @return the world holder converted to the supplied type
     * @throws UnsupportedOperationException when the wrapper could not be converted to its new type
     */
    public static <T> T convert(@NotNull World holder, @NotNull Class<T> newType) {
        if (worlds == null) {
            throw new UnsupportedOperationException("Worlds is not initialized yet.");
        }
        return worlds.converter.convert(holder, newType);
    }


    /**
     * Gets the world holder by a supplied {@link UUID}.
     *
     * @param uuid the world uuid
     * @return the world, can be empty
     */
    @Contract("null -> null")
    public static @Nullable World getWorld(@Nullable UUID uuid) {
        if (worlds == null) {
            throw new UnsupportedOperationException("Worlds is not initialized yet.");
        }
        if (uuid == null) {
            return null;
        }
        return worlds.getWorld0(uuid);
    }

    /**
     * Gets the world holder by a supplied name.
     *
     * @param name the world name
     * @return the world, can be empty
     */
    @Contract("null -> null")
    public static @Nullable World getWorld(@Nullable String name) {
        if (worlds == null) {
            throw new UnsupportedOperationException("Worlds is not initialized yet.");
        }
        if (name == null) {
            return null;
        }
        return worlds.getWorld0(name);
    }

    // abstract methods for implementations

    protected abstract @Nullable World getWorld0(@NotNull UUID uuid);

    protected abstract @Nullable World getWorld0(@NotNull String name);
}
