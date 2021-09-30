package org.screamingsandals.lib;

import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.world.WorldHolder;

import java.util.List;

/**
 * <p>Class holding various methods for manipulating with the Minecraft server.</p>
 */
@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<className>.+)$",
        replaceRule = "{basePackage}.{platform}.{Platform}{className}"
)
public abstract class Server {
    /**
     * <p>Server class instance.</p>
     */
    private static Server server;

    /**
     * <p>Constructs the Server class, internal use only.</p>
     */
    public Server() {
        if (server != null) {
            throw new UnsupportedOperationException("Server has been already initialized!");
        }
        server = this;
    }

    /**
     * <p>Returns a boolean stating if the current thread is the server thread.</p>
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
     * <p>Returns the version string of the current Minecraft server.</p>
     *
     * @return the version string (1.17.1 for example)
     */
    public static String getVersion() {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.getVersion0();
    }

    /**
     * <p>Compares the server version with a supplied value.</p>
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
     * <p>Compares the server version with a supplied value.</p>
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
     * <p>Gets the list of players that are currently connected to the server.</p>
     *
     * @return list of players currently connected to the server
     */
    public static List<PlayerWrapper> getConnectedPlayers() {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.getConnectedPlayers0();
    }

    /**
     * <p>Gets a list of players that are currently in the supplied world.</p>
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
     * <p>Runs a {@link Runnable} synchronously (on the main server thread).</p>
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

    // abstract methods for implementations

    public abstract String getVersion0();

    public abstract boolean isVersion0(int major, int minor);

    public abstract boolean isVersion0(int major, int minor, int patch);

    public abstract boolean isServerThread0();

    public abstract List<PlayerWrapper> getConnectedPlayers0();

    public abstract List<PlayerWrapper> getConnectedPlayersFromWorld0(WorldHolder world);

    public abstract void runSynchronously0(Runnable task);
}
