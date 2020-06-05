package org.screamingsandals.lib.gamecore.visuals;

import org.screamingsandals.lib.gamecore.core.GameState;

public interface GameVisual {

    void update();

    GameState getGameState();

    VisualType getVisualType();

    void show();

    void hide();
}
