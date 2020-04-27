package org.screamingsandals.lib.gamecore.core.phase;

import lombok.Data;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.core.cycle.GameCycle;

@Data
public abstract class GamePhase {
    private final GameCycle gameCycle;
    private final GameFrame gameFrame;
    private final GameState phaseType;
    private final int runTime;
    private int elapsedTime;

    public GamePhase(GameCycle gameCycle, GameState phaseType, int runTime) {
        this.gameCycle = gameCycle;
        this.gameFrame = gameCycle.getGameFrame();
        this.phaseType = phaseType;
        this.runTime = runTime;

        prepare(gameFrame);
    }

    public void tick() {
        elapsedTime++;
    }

    public void prepare(GameFrame gameFrame) {

    }

    public boolean hasFinished() {
        return elapsedTime == runTime;
    }

    public int countRemainingTime() {
        return runTime - elapsedTime;
    }
}
