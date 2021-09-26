package org.screamingsandals.lib;

import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.world.WorldHolder;

import java.util.List;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<className>.+)$",
        replaceRule = "{basePackage}.{platform}.{Platform}{className}"
)
public abstract class Server {
    private static Server server;

    public Server() {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
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

    public static boolean isVersion(int major, int minor) {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.isVersion0(major, minor);
    }

    public static boolean isVersion(int major, int minor, int patch) {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.isVersion0(major, minor, patch);
    }

    public static List<PlayerWrapper> getConnectedPlayers() {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        return server.getConnectedPlayers0();
    }

    public static List<PlayerWrapper> getConnectedPlayersFromWorld(WorldHolder world) {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        if (world == null) {
            throw new UnsupportedOperationException("Invalid world provided!");
        }
        return server.getConnectedPlayersFromWorld0(world);
    }

    public static void runSynchronously(Runnable task) {
        if (server == null) {
            throw new UnsupportedOperationException("Server has not yet been initialized!");
        }
        if (task == null) {
            throw new UnsupportedOperationException("Invalid task provided!");
        }
        server.runSynchronously0(task);
    }

    public abstract boolean isVersion0(int major, int minor);

    public abstract boolean isVersion0(int major, int minor, int patch);

    public abstract boolean isServerThread0();

    public abstract List<PlayerWrapper> getConnectedPlayers0();

    public abstract List<PlayerWrapper> getConnectedPlayersFromWorld0(WorldHolder world);

    public abstract void runSynchronously0(Runnable task);
}
