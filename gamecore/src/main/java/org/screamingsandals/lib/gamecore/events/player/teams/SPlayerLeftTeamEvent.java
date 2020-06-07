package org.screamingsandals.lib.gamecore.events.player.teams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.events.BaseEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.gamecore.team.GameTeam;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerLeftTeamEvent extends BaseEvent {
    private final GameFrame gameFrame;
    private final GamePlayer gamePlayer;
    private final GameTeam gameTeam;
}
