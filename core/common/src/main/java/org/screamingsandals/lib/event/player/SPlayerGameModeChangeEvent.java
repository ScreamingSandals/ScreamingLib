package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;

public interface SPlayerGameModeChangeEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    GameModeHolder getGameMode();
}
