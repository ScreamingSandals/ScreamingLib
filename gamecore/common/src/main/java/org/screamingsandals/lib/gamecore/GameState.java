package org.screamingsandals.lib.gamecore;

public interface GameState {
    boolean isGameRunning();

    boolean isGameRegenerating();

    boolean isLobby();

    boolean isDisabled();

    boolean isEdit();
}
