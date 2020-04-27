package org.screamingsandals.gamecore;

import io.papermc.lib.PaperLib;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.gamecore.core.GameFrame;
import org.screamingsandals.gamecore.core.GameManager;
import org.screamingsandals.gamecore.events.core.SCoreLoadedEvent;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.tasker.Tasker;

import java.io.File;

@Data
public class GameCore {
    private final Plugin plugin;
    private static GameCore instance;
    private GameManager<?> gameManager;
    private final Tasker tasker;

    public GameCore(Plugin plugin) {
        this.plugin = plugin;
        instance = this;

        Debug.setFallbackName("GameCore");

        tasker = Tasker.getSpigot(plugin);
    }

    public <T extends GameFrame> void load(File gamesFolder, Class<T> tClass) {
        GameManager<T> gameManager = new GameManager<>(gamesFolder, tClass);
        this.gameManager = gameManager;

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
