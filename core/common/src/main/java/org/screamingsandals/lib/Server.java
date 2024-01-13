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

package org.screamingsandals.lib;

import io.netty.channel.ChannelFuture;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.player.Sender;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.ProxyType;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.world.World;
import org.screamingsandals.lib.world.Worlds;

import java.util.List;

/**
 * Class holding various methods for manipulating with the Minecraft server.
 */
@ProvidedService
public abstract class Server {
    private static @Nullable Server server;
    private static @Nullable Integer PROTOCOL_VERSION;

    @ApiStatus.Internal
    public Server() {
        Preconditions.checkArgument(server == null, "Server has been already initialized!");
        server = this;
    }

    /**
     * Returns a boolean stating if the current thread is the server thread.
     *
     * @return true if current thread is same as the Server thread, false otherwise
     */
    public static boolean isServerThread() {
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").isServerThread0();
    }

    /**
     * Returns the version string of the current Minecraft server.
     *
     * @return the version string (1.17.1 for example)
     */
    public static @NotNull String getVersion() {
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").getVersion0();
    }

    public static @NotNull String getServerSoftwareVersion() {
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").getServerSoftwareVersion0();
    }

    /**
     * Compares the server version with a supplied value.
     *
     * @param major major version number (for example, 1.16.5 -> 1)
     * @param minor minor version number (for example, 1.16.5 -> 16)
     * @return is this server version matching the supplied version?
     */
    public static boolean isVersion(int major, int minor) {
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").isVersion0(major, minor);
    }

    /**
     * Compares the server version with a supplied value.
     *
     * @param major major version number (for example, 1.16.5 -> 1)
     * @param minor minor version number (for example, 1.16.5 -> 16)
     * @param patch patch version number (for example, 1.16.5 -> 5)
     * @return is this server version matching the supplied version?
     */
    public static boolean isVersion(int major, int minor, int patch) {
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").isVersion0(major, minor, patch);
    }

    /**
     * Gets the list of players that are currently connected to the server.
     *
     * @return list of players currently connected to the server
     */
    public static @NotNull List<@NotNull Player> getConnectedPlayers() {
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").getConnectedPlayers0();
    }

    /**
     * Gets a list of players that are currently in the supplied world.
     *
     * @param world the world
     * @return list of players currently in the world
     */
    public static @NotNull List<@NotNull Player> getConnectedPlayersFromWorld(@NotNull World world) {
        Preconditions.checkNotNull(server, "Server has not yet been initialized!");
        Preconditions.checkNotNull(world, "Invalid world provided!");
        return server.getConnectedPlayersFromWorld0(world);
    }

    public static void shutdown() {
        Preconditions.checkNotNull(server, "Server has not yet been initialized!").shutdown0();
    }

    public static @NotNull ProxyType getProxyType() {
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").getProxyType0();
    }

    public static @NotNull Integer getProtocolVersion() {
        if (PROTOCOL_VERSION == null) {
            PROTOCOL_VERSION = Preconditions.checkNotNull(server, "Server has not yet been initialized!").getProtocolVersion0();
        }
        return PROTOCOL_VERSION;
    }

    public static @NotNull Sender getConsoleSender() {
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").getConsoleSender0();
    }

    // abstract methods for implementations

    public abstract @NotNull String getVersion0();

    public abstract @NotNull String getServerSoftwareVersion0();

    public static List<@NotNull ChannelFuture> getConnections() {
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").getConnections0();
    }

    public abstract boolean isVersion0(int major, int minor);

    public abstract boolean isVersion0(int major, int minor, int patch);

    public abstract boolean isServerThread0();

    public abstract @NotNull List<@NotNull Player> getConnectedPlayers0();

    public abstract @NotNull List<@NotNull Player> getConnectedPlayersFromWorld0(@NotNull World world);

    public abstract List<@NotNull ChannelFuture> getConnections0();

    public abstract @NotNull Sender getConsoleSender0();

    public abstract void shutdown0();

    public abstract @NotNull ProxyType getProxyType0();

    public abstract @NotNull Integer getProtocolVersion0();
}
