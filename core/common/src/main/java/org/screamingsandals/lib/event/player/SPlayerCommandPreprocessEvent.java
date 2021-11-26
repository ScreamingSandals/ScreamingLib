package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

public interface SPlayerCommandPreprocessEvent extends SCancellableEvent, SPlayerEvent {
    void setPlayer(PlayerWrapper player);

    String getCommand();

    void setCommand(String command);
}
