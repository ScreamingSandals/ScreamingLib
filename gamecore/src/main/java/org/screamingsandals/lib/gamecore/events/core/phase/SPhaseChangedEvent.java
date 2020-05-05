package org.screamingsandals.lib.gamecore.events.core.phase;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.phase.GamePhase;
import org.screamingsandals.lib.gamecore.events.BaseEvent;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPhaseChangedEvent extends BaseEvent {
    private final GameFrame gameFrame;
    private final GamePhase currentPhase;
    private final GamePhase previousPhase;
}
