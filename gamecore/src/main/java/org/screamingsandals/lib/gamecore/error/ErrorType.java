package org.screamingsandals.lib.gamecore.error;

import java.io.Serializable;

public enum ErrorType implements Serializable {
    GAME_CORE_ERROR("core.errors.game-core"),
    GAME_CYCLE_IS_NULL("core.errors.game-cycle-null"),
    GAME_CONFIG_ERROR("core.errors.game-config"),
    GAME_SHOP_ERROR("core.errors.game-store"),
    GAME_LOADING_ERROR("core.errors.game-loading"),
    GAME_WORLD_NOT_DEFINED("core.errors.game-world-not-defined"),
    GAME_WORLD_DOES_NOT_EXISTS("core.errors.game-world-does-not-exists"),
    GAME_WORLD_BAD_BORDER("core.errors.game-world-bad-border"),
    LOBBY_WORLD_NOT_DEFINED("core.errors.lobby-world-not-defined"),
    LOBBY_WORLD_DOES_NOT_EXISTS("core.errors.lobby-world-does-not-exists"),
    LOBBY_WORLD_BAD_BORDER("core.errors.lobby-world-bad-border"),
    LOBBY_SPAWN_NOT_SET("core.errors.lobby-spawn-not-set"),
    SPAWNER_EDITOR_FAILED("core.errors.spawner-editor-failed"),
    SPECTATOR_SPAWN_NOT_SET("core.errors.spectator-spawn-not-set"),
    NOT_ENOUGH_TEAMS("core.errors.not-enough-teams"),
    TEAM_SPAWN_NOT_SET("core.errors.team-spawn-not-set"),
    NOT_ENOUGH_STORES("core.errors.not-enough-stores"),
    CONFIG_NOT_DEFINED("core.errors.config.not-defined"),
    CONFIG_WRONG_BOSSBAR_COLOR("core.errors.config.wrong-bossbar-color"),
    PREPARE_FAILED("core.errors.prepare-failed"),
    UNKNOWN("core.errors.unknown");

    private final String key;

    ErrorType(String key) {
        this.key = key;

    }

    public String getKey() {
        return key;
    }

}
