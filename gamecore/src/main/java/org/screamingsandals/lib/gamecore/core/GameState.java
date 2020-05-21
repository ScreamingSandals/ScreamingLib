package org.screamingsandals.lib.gamecore.core;

public enum GameState {
    LOADING("loading"),
    WAITING("lobby"),
    PRE_GAME_COUNTDOWN("starting"),
    IN_GAME("game"),
    DEATHMATCH("deathmatch"),
    AFTER_GAME_COUNTDOWN("end_game"),
    RESTART("restart"),
    MAINTENANCE("maintenance"),
    CUSTOM("custom"),
    DISABLED("disabled");

    private final String name;

    GameState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
