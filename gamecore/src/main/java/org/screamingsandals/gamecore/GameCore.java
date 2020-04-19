package org.screamingsandals.gamecore;

import lombok.Data;
import org.screamingsandals.gamecore.core.GameManager;
import org.screamingsandals.lib.debug.Debug;

@Data
public class GameCore {
    private static GameCore instance;
    private GameManager gameManager;

    public GameCore() {
        instance = this;

        Debug.setFallbackName("GameCore");
    }

    public static GameManager getGameManager() {
        return instance.gameManager;
    }
}
