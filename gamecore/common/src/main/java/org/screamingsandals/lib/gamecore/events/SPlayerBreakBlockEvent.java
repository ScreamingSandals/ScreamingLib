package org.screamingsandals.lib.gamecore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.gamecore.Game;
import org.screamingsandals.lib.gamecore.players.GamePlayer;
import org.screamingsandals.lib.gamecore.team.Team;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class SPlayerBreakBlockEvent<P extends GamePlayer<P, G>, G extends Game<G, P, ?>, T extends Team<P, G>> extends CancellableAbstractEvent {
    private final G game;
    private final P player;
    private final T team;
    private final BlockHolder block;
    private boolean drops;
}
