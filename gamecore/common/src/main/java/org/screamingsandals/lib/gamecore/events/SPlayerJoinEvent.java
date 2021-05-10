package org.screamingsandals.lib.gamecore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.gamecore.Game;
import org.screamingsandals.lib.gamecore.players.GamePlayer;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class SPlayerJoinEvent<P extends GamePlayer<P, G>, G extends Game<G, P, ?>> extends CancellableAbstractEvent {
    private final G game;
    private final P player;
    @Nullable
    private String cancelMessage;
}
