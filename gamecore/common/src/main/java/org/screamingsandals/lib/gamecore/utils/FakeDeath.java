package org.screamingsandals.lib.gamecore.utils;

import org.screamingsandals.lib.gamecore.Game;
import org.screamingsandals.lib.gamecore.players.GamePlayer;

public interface FakeDeath<G extends Game<G, P, ?>, P extends GamePlayer<P, G>> {
    void die(P gamePlayer);
}
