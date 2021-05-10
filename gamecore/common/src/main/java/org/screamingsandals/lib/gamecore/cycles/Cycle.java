package org.screamingsandals.lib.gamecore.cycles;

import org.screamingsandals.lib.gamecore.GameState;
import org.screamingsandals.lib.tasker.TaskHolder;

public abstract class Cycle<GS extends GameState> extends TaskHolder {
    public abstract GS getGameState();
}
