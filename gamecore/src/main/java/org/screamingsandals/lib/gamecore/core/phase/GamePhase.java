package org.screamingsandals.lib.gamecore.core.phase;

import lombok.Data;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.core.cycle.GameCycle;

@Data
public abstract class GamePhase {
    protected final transient GameCycle gameCycle;
    protected final transient GameFrame gameFrame;
    protected final int runTime;
    protected GameState phaseType;
    protected int elapsedTime;

    public GamePhase(GameCycle gameCycle, int runTime) {
        this.gameCycle = gameCycle;
        this.gameFrame = gameCycle.getGameFrame();
        this.runTime = runTime;

        prepare(gameFrame);
    }

    public void tick() {
        elapsedTime++;
    }

    public void prepare(GameFrame gameFrame) {

    }

    public boolean hasFinished() {
        if (runTime == -1 && elapsedTime == 1) {
            return true;
        }
        return elapsedTime == runTime;
    }

    public int countRemainingTime() {
        if (runTime == -1 && elapsedTime == 1) {
            return 0;
        }

        return runTime - elapsedTime;
    }

    public void reset() {
        elapsedTime = 0;
    }

}
