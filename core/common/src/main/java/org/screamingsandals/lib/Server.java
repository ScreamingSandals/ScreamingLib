package org.screamingsandals.lib;

import io.netty.channel.ChannelFuture;
import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.player.PlayerWrapper;
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

    @ApiStatus.Internal
    public Server() {
        if (server != null) {
            throw new UnsupportedOperationException("Server has been already initialized!");
        }
        server = this;
    }

    /**
     * Returns a boolean stating if the current thread is the server thread.
     *
     * @return true if current thread is same as the Server thread, false otherwise
     */
    public static boolean isServerThread() {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.isServerThread0();
    }

    /**
     * Returns the version string of the current Minecraft server.
     *
     * @return the version string (1.17.1 for example)
     */
    public static String getVersion() {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.getVersion0();
    }

    public static String getServerSoftwareVersion() {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.getServerSoftwareVersion0();
    }

    /**
     * Compares the server version with a supplied value.
     *
     * @param major major version number (for example, 1.16.5 -> 1)
     * @param minor minor version number (for example, 1.16.5 -> 16)
     * @return is this server version matching the supplied version?
     */
    public static boolean isVersion(int major, int minor) {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.isVersion0(major, minor);
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
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.isVersion0(major, minor, patch);
    }

    /**
     * Gets the list of players that are currently connected to the server.
     *
     * @return list of players currently connected to the server
     */
    public static List<PlayerWrapper> getConnectedPlayers() {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.getConnectedPlayers0();
    }

    public static List<WorldHolder> getWorlds() {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.getWorlds0();
    }

    /**
     * Gets a list of players that are currently in the supplied world.
     *
     * @param world the world
     * @return list of players currently in the world
     */
    public static List<PlayerWrapper> getConnectedPlayersFromWorld(WorldHolder world) {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        if (world == null) {
            throw new UnsupportedOperationException("Invalid world provided!");
        }
        return server.getConnectedPlayersFromWorld0(world);
    }

    /**
     * Runs a {@link Runnable} synchronously (on the main server thread).
     *
     * @param task the runnable
     */
    public static void runSynchronously(Runnable task) {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        if (task == null) {
            throw new UnsupportedOperationException("Invalid task provided!");
        }
        server.runSynchronously0(task);
    }

    @ApiStatus.Internal
    @ApiStatus.Experimental
    public static String UNSAFE_normalizeSoundKey(String s) {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.UNSAFE_normalizeSoundKey0(s);
    }

    public static void shutdown() {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        server.shutdown0();
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
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        server.UNSAFE_earlyInitializeLegacySupportAndIgnoreItsUsage0();
    }

    // abstract methods for implementations

    public abstract String getVersion0();

    public abstract String getServerSoftwareVersion0();

    public static List<ChannelFuture> getConnections() {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.getConnections0();
    }

    public abstract boolean isVersion0(int major, int minor);

    public abstract boolean isVersion0(int major, int minor, int patch);

    public abstract boolean isServerThread0();

    public abstract List<PlayerWrapper> getConnectedPlayers0();

    public abstract List<PlayerWrapper> getConnectedPlayersFromWorld0(WorldHolder world);

    public abstract List<WorldHolder> getWorlds0();

    public abstract void runSynchronously0(Runnable task);

    public abstract List<ChannelFuture> getConnections0();

    public abstract void shutdown0();

    public String UNSAFE_normalizeSoundKey0(String s) {
        return s;
    }

    @ApiStatus.OverrideOnly
    public void UNSAFE_earlyInitializeLegacySupportAndIgnoreItsUsage0() {
    }
}
