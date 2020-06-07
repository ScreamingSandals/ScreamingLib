package org.screamingsandals.lib.gamecore.events.player.spectator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Cancellable;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.events.BaseEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

@EqualsAndHashCode(callSuper = false)
@Data
public class SSpectatorPreJoinGameEvent extends BaseEvent implements Cancellable {
    private final GameFrame gameFrame;
    private final GamePlayer gamePlayer;
    private boolean cancelled;
}

