package org.screamingsandals.lib.gamecore.error;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ErrorType {

    GAME_CORE_ERROR("core.errors.game-core", "&cSomething went wrong with the GameCore instance. Please report this error log to GitHub or Discord!", Collections.emptyMap()),
    GAME_CONFIG_ERROR("core.errors.game-config", "&cGameConfiguration is wrong. Please report this error log to GitHub or Discord!", Collections.emptyMap()),
    GAME_SHOP_ERROR("core.errors.game-store", "&cYour shop configuration is invalid. Please check validity of your current file.", Collections.emptyMap()),
    GAME_LOADING_ERROR("core.errors.game-loading", "&c&lOh, damn. &cLoading some arena file went wrong. &a&lPlease report this error log to GitHub or Discord!", new HashMap<>()),
    GAME_WORLD_NOT_DEFINED("core.errors.game-world-not-defined", "&cGame world for the game &e%gameName% &cis not defined!", new HashMap<>()),
    GAME_WORLD_DOES_NOT_EXISTS("core.errors.game-world-does-not-exists", "&cGame world &e%world% for the game &e%gameName% &cdoes not exists!", new HashMap<>()),
    GAME_WORLD_BAD_BORDER("core.errors.game-world-bad-border", "&cBorder for the game &e%gameName% &cis wrong. &lPlease re-do it!", new HashMap<>()),
    LOBBY_WORLD_NOT_DEFINED("core.errors.lobby-world-not-defined", "&cLobby world for the game &e%gameName% &cis not defined!", new HashMap<>()),
    LOBBY_WORLD_DOES_NOT_EXISTS("core.errors.lobby-world-does-not-exists", "&cLobby world &e%world% &cfor the game &e%gameName% &cdoes not exists!", new HashMap<>()),
    LOBBY_WORLD_BAD_BORDER("core.errors.lobby-world-bad-border", "&cBorder for the lobby of the game &e%gameName% &cis wrong. &lPlease re-do it!", new HashMap<>()),
    LOBBY_SPAWN_NOT_SET("core.errors.lobby-spawn-not-set", "&cBorder for the lobby of the game &e%gameName% &cis wrong. &lPlease re-do it!", new HashMap<>()),
    SPAWNER_EDITOR_FAILED("core.errors.spawner-editor-failed", "&cThis, we did not expected. &a&lPlease report this error log to GitHub or Discord!", new HashMap<>()),
    SPECTATOR_SPAWN_NOT_SET("core.errors.spectator-spawn-not-set", "&cSpectator spawn for the game %gameName% is not set!", new HashMap<>()),
    NOT_ENOUGH_TEAMS("core.errors.not-enough-teams", "&cNot enough teams defined for the game &e%gameName%. &c&lYou need at least 2!", new HashMap<>()),
    NOT_ENOUGH_STORES("core.errors.not-enough-stores", "&cNot enough stores defined for the game &e%gameName%. &c&lYou need at least one!", new HashMap<>()),
    PREPARE_FAILED("core.errors.prepare-failed", "&cPrepare phase failed, can't start the game &e%gameName%&c!", new HashMap<>()),
    UNKNOWN("core.errors.unknown", "&cUnknown error occurred. Error code printed to console, please report it to our GitHub or Discord!", Collections.emptyMap());

    private final Map<String, Object> replaceable;
    private final String languageKey;
    private final String defaultMessage;

    ErrorType(String languageKey, String defaultMessage, Map<String, Object> replaceable) {
        this.languageKey = languageKey;
        this.defaultMessage = defaultMessage;
        this.replaceable = replaceable;
    }

    public String getLanguageKey() {
        return languageKey;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public Map<String, Object> getReplaceable() {
        return replaceable;
    }

}
