package org.screamingsandals.lib.gamecore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.gamecore.Game;
import org.screamingsandals.lib.gamecore.players.GamePlayer;
import org.screamingsandals.lib.gamecore.team.Team;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class SPlayerLastLeaveEvent<P extends GamePlayer<P, G>, G extends Game<G, P, ?>, T extends Team<P, G>> extends AbstractEvent {
    private final G game;
    private final P player;
    @Nullable
    private final T team;
}
