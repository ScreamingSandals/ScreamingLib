package org.screamingsandals.lib.gamecore.events.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.gamecore.events.BaseEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerSwitchedToSpectator extends BaseEvent {
    private final GamePlayer gamePlayer;
}
