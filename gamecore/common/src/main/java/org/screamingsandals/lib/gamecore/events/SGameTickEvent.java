package org.screamingsandals.lib.gamecore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.gamecore.Game;
import org.screamingsandals.lib.gamecore.GameState;
import org.screamingsandals.lib.gamecore.players.GamePlayer;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class SGameTickEvent<P extends GamePlayer<P, G>, G extends Game<G, P, GS>, GS extends GameState> extends AbstractEvent {
    private final G game;
    private final int previousCountdown;
    private final GS previousStatus;
    private final int countdown;
    private final GS status;
    private final int originalNextCountdown;
    private final GS originalNextStatus;
    private int nextCountdown;
    private GS nextStatus;

    public void preventContinuation(boolean prevent) {
        if (prevent) {
            this.nextCountdown = this.countdown;
            this.nextStatus = this.status;
        } else {
            this.nextCountdown = this.originalNextCountdown;
            this.nextStatus = this.originalNextStatus;
        }
    }

    public boolean isNextCountdownChanged() {
        return this.nextCountdown != this.originalNextCountdown;
    }

    public boolean isNextStatusChanged() {
        return this.nextStatus != this.originalNextStatus;
    }
}
