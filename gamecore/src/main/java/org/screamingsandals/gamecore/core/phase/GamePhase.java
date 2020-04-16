package org.screamingsandals.gamecore.core.phase;

import lombok.Data;
import org.screamingsandals.gamecore.core.GameFrame;
import org.screamingsandals.gamecore.core.GameState;
import org.screamingsandals.gamecore.core.cycle.GameCycle;

@Data
public abstract class GamePhase {
    private final GameCycle gameCycle;
    private final GameFrame gameFrame;
    private final GameState phaseType;
    private int elapsedTime;
    private int remainingTime;
    private int runTime;

    public GamePhase(GameCycle gameCycle, GameState phaseType) {
        this.gameCycle = gameCycle;
        this.gameFrame = gameCycle.getGameFrame();
        this.phaseType = phaseType;

        prepare(gameFrame);
    }

    public void tick() {
        elapsedTime++;
        remainingTime = runTime - elapsedTime;
    }

    public void prepare(GameFrame gameFrame) {

    }

    public boolean hasFinished() {
        return remainingTime > runTime;
    }
}
