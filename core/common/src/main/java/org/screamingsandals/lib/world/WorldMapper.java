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
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

import java.util.UUID;

/**
 * Class responsible for converting platform worlds to wrappers.
 */
@AbstractService
@ExtensionMethod(value = {NullableExtension.class}, suppressBaseMethods = false)
public abstract class WorldMapper {
    protected BidirectionalConverter<WorldHolder> converter = BidirectionalConverter.<WorldHolder>build()
            .registerP2W(WorldHolder.class, e -> e);

    private static WorldMapper mapping;

    /**
     * Constructs the location mapper.
     */
    @ApiStatus.Internal
    public WorldMapper() {
        if (mapping != null) {
            throw new UnsupportedOperationException("WorldMapper is already initialized.");
        }
        mapping = this;
    }

    /**
     * Resolves the supplied platform world to its {@link WorldHolder} wrapper, can be empty.
     *
     * @param obj the platform world
     * @return the world wrapper
     */
    @Contract("null -> null")
    public static @Nullable WorldHolder resolve(@Nullable Object obj) {
        if (mapping == null) {
            throw new UnsupportedOperationException("WorldMapper is not initialized yet.");
        }
        return mapping.converter.convertNullable(obj);
    }

    /**
     * Maps the supplied platform world to its {@link WorldHolder} wrapper.
     *
     * @param input the platform world
     * @param <T> the platform world type
     * @return the world wrapper
     * @throws java.util.NoSuchElementException when the world could not be mapped
     */
    public static <T> @NotNull WorldHolder wrapWorld(@NotNull T input) {
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
    public static <T> T convert(WorldHolder holder, Class<T> newType) {
        if (mapping == null) {
            throw new UnsupportedOperationException("WorldMapper is not initialized yet.");
        }
        return mapping.converter.convert(holder, newType);
    }


    /**
     * Gets the world holder by a supplied {@link UUID}.
     *
     * @param uuid the world uuid
     * @return the world, can be empty
     */
    @Contract("null -> null")
    public static @Nullable WorldHolder getWorld(@Nullable UUID uuid) {
        if (mapping == null) {
            throw new UnsupportedOperationException("WorldMapper is not initialized yet.");
        }
        if (uuid == null) {
            return null;
        }
        return mapping.getWorld0(uuid);
    }

    /**
     * Gets the world holder by a supplied name.
     *
     * @param name the world name
     * @return the world, can be empty
     */
    @Contract("null -> null")
    public static @Nullable WorldHolder getWorld(@Nullable String name) {
        if (mapping == null) {
            throw new UnsupportedOperationException("WorldMapper is not initialized yet.");
        }
        if (name == null) {
            return null;
        }
        return mapping.getWorld0(name);
    }

    // abstract methods for implementations

    protected abstract @Nullable WorldHolder getWorld0(@NotNull UUID uuid);

    protected abstract @Nullable WorldHolder getWorld0(@NotNull String name);
}
