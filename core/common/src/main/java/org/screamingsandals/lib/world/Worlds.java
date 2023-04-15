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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.annotations.ProvidedService;

import java.util.List;
import java.util.UUID;

/**
 * Class responsible for converting platform worlds to wrappers.
 */
@ProvidedService
public abstract class Worlds {
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

    /**
     * Gets the list of worlds that are currently loaded pn the server.
     *
     * @return list of worlds currently loaded on the server
     */
    public static @NotNull List<@NotNull World> getWorlds() {
        if (worlds == null) {
            throw new UnsupportedOperationException("Worlds is not initialized yet.");
        }
        return worlds.getWorlds0();
    }

    // abstract methods for implementations

    protected abstract @Nullable World getWorld0(@NotNull UUID uuid);

    protected abstract @Nullable World getWorld0(@NotNull String name);

    protected abstract @NotNull List<@NotNull World> getWorlds0();
}
