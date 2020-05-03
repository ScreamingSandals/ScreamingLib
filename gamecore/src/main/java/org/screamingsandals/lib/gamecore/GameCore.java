package org.screamingsandals.lib.gamecore;

import io.papermc.lib.PaperLib;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameManager;
import org.screamingsandals.lib.gamecore.error.ErrorManager;
import org.screamingsandals.lib.gamecore.events.core.SCoreLoadedEvent;
import org.screamingsandals.lib.gamecore.events.core.SCoreUnloadedEvent;
import org.screamingsandals.lib.gamecore.exceptions.GameCoreException;
import org.screamingsandals.lib.gamecore.listeners.player.PlayerListener;
import org.screamingsandals.lib.gamecore.player.PlayerManager;
import org.screamingsandals.lib.tasker.Tasker;

import java.io.File;

@Data
public class GameCore {
    private static GameCore instance;
    private final Plugin plugin;
    private final Tasker tasker;
    private final ErrorManager errorManager;
    private final PlayerManager playerManager;
    private GameManager<?> gameManager;

    public GameCore(Plugin plugin) {
        this.plugin = plugin;
        instance = this;

        tasker = Tasker.getSpigot(plugin);
        errorManager = new ErrorManager();
        playerManager = new PlayerManager();

        Debug.setFallbackName("GameCore");
    }

    public <T extends GameFrame> void load(File gamesFolder, Class<T> tClass) throws GameCoreException {
        try {
            this.gameManager = new GameManager<>(gamesFolder, tClass);

            plugin.getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);

            fireEvent(new SCoreLoadedEvent(this));
        } catch (Exception exception) {
            errorManager.newError(ErrorManager.newEntry(ErrorManager.Type.GAME_CORE_ERROR, exception));
            throw new GameCoreException("Whoopsie, something went wrong with GameCore!");
        }
    }

    public void destroy() {
        gameManager.unregisterGames();
        tasker.destroy();
        errorManager.destroy();

        fireEvent(new SCoreUnloadedEvent(this));
    }

    public static GameCore getInstance() {
        return instance;
    }

    public static GameManager<?> getGameManager() {
        return instance.gameManager;
    }

    public static PlayerManager getPlayerManager() {
        return instance.playerManager;
    }

    public static Plugin getPlugin() {
        return instance.plugin;
    }

    public static boolean fireEvent(Event event) {
        if (PaperLib.isPaper()) {
            return event.callEvent();
        } else {
            Bukkit.getPluginManager().callEvent(event);
            if (event instanceof Cancellable) {
                return !((Cancellable) event).isCancelled();
            } else {
                return true;
            }
        }
    }
}
