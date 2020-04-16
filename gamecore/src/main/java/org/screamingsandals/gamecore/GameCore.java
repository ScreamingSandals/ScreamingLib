package org.screamingsandals.gamecore;

import lombok.Data;
import org.screamingsandals.gamecore.core.GameManager;

@Data
public class GameCore {
    private static GameCore instance;
    private GameManager gameManager;

    public GameCore() {
        instance = this;
    }

    public static GameManager getGameManager() {
        return instance.gameManager;
    }
}
