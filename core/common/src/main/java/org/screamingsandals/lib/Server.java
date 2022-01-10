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

package org.screamingsandals.lib;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.Version;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.world.WorldHolder;

import java.util.List;

/**
 * Class holding various methods for manipulating with the Minecraft server.
 */
@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<className>.+)$",
        replaceRule = "{basePackage}.{platform}.{Platform}{className}"
)
public abstract class Server {
    private static Server server;
    private static Integer PROTOCOL_VERSION = null;

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
    public static Version getVersion() {
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").getVersion0();
    }

    public static String getServerSoftwareVersion() {
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
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").getVersion0().isVersion(major, minor);
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
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").getVersion0().isVersion(major, minor, patch);
    }

    /**
     * Gets the list of players that are currently connected to the server.
     *
     * @return list of players currently connected to the server
     */
    public static List<PlayerWrapper> getConnectedPlayers() {
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").getConnectedPlayers0();
    }

    public static List<WorldHolder> getWorlds() {
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").getWorlds0();
    }

    /**
     * Gets a list of players that are currently in the supplied world.
     *
     * @param world the world
     * @return list of players currently in the world
     */
    public static List<PlayerWrapper> getConnectedPlayersFromWorld(@NotNull WorldHolder world) {
        Preconditions.checkNotNull(server, "Server has not yet been initialized!");
        Preconditions.checkNotNull(world, "Invalid world provided!");
        return server.getConnectedPlayersFromWorld0(world);
    }

    /**
     * Runs a {@link Runnable} synchronously (on the main server thread).
     *
     * @param task the runnable
     */
    public static void runSynchronously(@NotNull Runnable task) {
        Preconditions.checkNotNull(server, "Server has not yet been initialized!");
        Preconditions.checkNotNull(task, "Invalid task provided!");
        server.runSynchronously0(task);
    }

    @ApiStatus.Internal
    @ApiStatus.Experimental
    public static String UNSAFE_normalizeSoundKey(String s) {
        return Preconditions.checkNotNull(server, "Server has not yet been initialized!").UNSAFE_normalizeSoundKey0(s);
    }

    public static void shutdown() {
        Preconditions.checkNotNull(server, "Server has not yet been initialized!").shutdown0();
    }

    public static Integer getProtocolVersion() {
        if (PROTOCOL_VERSION == null) {
            PROTOCOL_VERSION = Preconditions.checkNotNull(server, "Server has not yet been initialized!").getProtocolVersion0();
        }
        return PROTOCOL_VERSION;
    }

    /**
     * <pre>
     *  O   This is Paul.
     * /|\  He checks the server version to decide
     *  |   when to use legacy method or flattening method.
     * / \  Be like Paul. Ignore this method.
     * </pre>
     */
    @ApiStatus.Experimental
    public static void UNSAFE_earlyInitializeLegacySupportAndIgnoreItsUsage() {
        Preconditions.checkNotNull(server, "Server has not yet been initialized!").UNSAFE_earlyInitializeLegacySupportAndIgnoreItsUsage0();
    }

    // abstract methods for implementations

    public abstract Version getVersion0();

    public abstract String getServerSoftwareVersion0();

    public abstract boolean isServerThread0();

    public abstract List<PlayerWrapper> getConnectedPlayers0();

    public abstract List<PlayerWrapper> getConnectedPlayersFromWorld0(WorldHolder world);

    public abstract List<WorldHolder> getWorlds0();

    public abstract void runSynchronously0(Runnable task);

    public abstract void shutdown0();

    public abstract Integer getProtocolVersion0();

    public String UNSAFE_normalizeSoundKey0(String s) {
        return s;
    }

    @ApiStatus.OverrideOnly
    public void UNSAFE_earlyInitializeLegacySupportAndIgnoreItsUsage0() {
    }
}
