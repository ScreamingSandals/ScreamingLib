package org.screamingsandals.gamecore;

import lombok.Data;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.gamecore.core.GameManager;
import org.screamingsandals.lib.debug.Debug;

@Data
public class GameCore {
    private final Plugin plugin;
    private static GameCore instance;
    private GameManager gameManager;

    public GameCore(Plugin plugin) {
        this.plugin = plugin;
        instance = this;

        Debug.setFallbackName("GameCore");
    }

    public static GameManager getGameManager() {
        return instance.gameManager;
    }

    public static Plugin getPlugin() {
        return instance.plugin;
    }
}
