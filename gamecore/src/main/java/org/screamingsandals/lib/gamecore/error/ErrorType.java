package org.screamingsandals.lib.gamecore.error;

import org.screamingsandals.lib.gamecore.core.GameFrame;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ErrorType {

    GAME_CORE_ERROR("core.errors.game_core", "&cSomething went wrong with the GameCore instance. Please report this error log to GitHub or Discord!", Collections.emptyMap()),
    GAME_CONFIG_ERROR("core.errors.game_config", "&cGameConfiguration is wrong. Please report this error log to GitHub or Discord!", Collections.emptyMap()),
    GAME_SHOP_ERROR("core.errors.game_shop", "&cYour shop configuration is invalid. Please check validity of your current file.", Collections.emptyMap()),
    GAME_LOADING_ERROR("core.errors.game_loading", "&c&lOh, damn. &cLoading some arena file went wrong. &a&lPlease report this error log to GitHub or Discord!", new HashMap<>()),
    GAME_WORLD_NOT_DEFINED("core.errors.game_world_does_not_exists", "&cGame world for the game &e%game% &cis not defined!", new HashMap<>()),
    GAME_WORLD_DOES_NOT_EXISTS("core.errors.game_world_does_not_exists", "&cGame world %world% for the game &e%game% &cdoes not exists!", new HashMap<>()),
    LOBBY_WORLD_DOES_NOT_EXISTS("core.errors.lobby_world_does_not_exists", "&cLobby world for the game &e%game% &cdoes not exists!", new HashMap<>()),
    SPAWNER_EDITOR_FAILED("core.errors.spawner_editor_failed", "&cThis, we did not expected. &a&lPlease report this error log to GitHub or Discord!", new HashMap<>()),
    UNKNOWN("core.errors.unknown", "&cUnknown error occurred. Error code printed to console, please report it to our GitHub or Discord!", Collections.emptyMap());

    private final Map<String, String> replaceable;
    private final String languageKey;
    private final String defaultMessage;

    ErrorType(String languageKey, String defaultMessage, Map<String, String> replaceable) {
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

    public Map<String, String> getReplaceable() {
        return replaceable;
    }

    public void replacePlaceholders(GameFrame gameFrame) {
        replaceable.put("%game%", gameFrame.getGameName());
    }

}
