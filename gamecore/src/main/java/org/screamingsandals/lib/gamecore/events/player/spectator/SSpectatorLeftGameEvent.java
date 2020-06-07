package org.screamingsandals.lib.gamecore.events.player.spectator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.events.BaseEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

@EqualsAndHashCode(callSuper = false)
@Data
public class SSpectatorLeftGameEvent extends BaseEvent {
    private final GameFrame gameFrame;
    private final GamePlayer gamePlayer;
}

