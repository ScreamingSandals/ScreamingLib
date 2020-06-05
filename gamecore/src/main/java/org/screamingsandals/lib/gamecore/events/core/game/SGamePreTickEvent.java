package org.screamingsandals.lib.gamecore.events.core.game;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Cancellable;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.cycle.GameCycle;
import org.screamingsandals.lib.gamecore.core.phase.GamePhase;
import org.screamingsandals.lib.gamecore.events.BaseEvent;

@EqualsAndHashCode(callSuper = false)
@Data
public class SGamePreTickEvent extends BaseEvent implements Cancellable {
    private final GameFrame gameFrame;
    private final GameCycle gameCycle;
    private final GamePhase currentPhase;
    private boolean cancelled;
}
