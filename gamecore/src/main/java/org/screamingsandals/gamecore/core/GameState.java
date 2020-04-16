package org.screamingsandals.gamecore.core;

public enum GameState {
    LOADING("loading"),
    WAITING("lobby"),
    PRE_GAME_COUNTDOWN("game"),
    IN_GAME("game"),
    DEATHMATCH("deathmatch"),
    AFTER_GAME_COUNTDOWN("game"),
    RESTART("restart"),
    MAINTENANCE("maintenance"),
    CUSTOM("custom"),
    DISABLED("disabled");

    private String name;

    GameState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
