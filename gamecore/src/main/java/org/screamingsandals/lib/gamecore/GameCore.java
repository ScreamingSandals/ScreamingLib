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
import org.screamingsandals.lib.tasker.Tasker;

import java.io.File;

@Data
public class GameCore {
    private final Plugin plugin;
    private final Tasker tasker;
    private final ErrorManager errorManager;
    private static GameCore instance;
    private GameManager<?> gameManager;

    public GameCore(Plugin plugin) {
        this.plugin = plugin;
        instance = this;

        tasker = Tasker.getSpigot(plugin);
        errorManager = new ErrorManager();

        Debug.setFallbackName("GameCore");
    }

    public <T extends GameFrame> void load(File gamesFolder, Class<T> tClass) {
        this.gameManager = new GameManager<>(gamesFolder, tClass);

        fireEvent(new SCoreLoadedEvent(this));
    }

    public static GameManager<?> getGameManager() {
        return instance.gameManager;
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
