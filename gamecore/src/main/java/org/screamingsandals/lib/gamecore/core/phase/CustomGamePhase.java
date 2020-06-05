package org.screamingsandals.lib.gamecore.core.phase;

import lombok.Getter;
import lombok.Setter;
import org.screamingsandals.lib.gamecore.core.cycle.GameCycle;

import java.util.UUID;

public abstract class CustomGamePhase extends GamePhase {
    @Getter
    @Setter
    protected UUID uuid; //phase identifier

    /**
     * Custom phase
     * @param gameCycle defined game cycle for the phase
     * @param runTime time for running
     */
    public CustomGamePhase(GameCycle gameCycle, int runTime) {
        super(gameCycle, runTime);
        this.uuid = UUID.randomUUID();
    }

    /**
     * Never ending custom phase
     * @param gameCycle defined game cycle for the phase
     */
    public CustomGamePhase(GameCycle gameCycle) {
        super(gameCycle, -1);
        this.uuid = UUID.randomUUID();
    }
}
