package org.screamingsandals.lib.gamecore.events.core.state;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.events.BaseEvent;

@EqualsAndHashCode(callSuper = false)
@Data
public class SGameStateChangedEvent extends BaseEvent {
    private final GameFrame gameFrame;
    private final GameState activeState;
    private final GameState previousState;
}