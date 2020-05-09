package org.screamingsandals.lib.gamecore.error;

import lombok.Data;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.core.GameFrame;

import java.util.*;

import static org.screamingsandals.lib.lang.I.m;

/**
 * Utility class for writing errors and logging them.
 * This will be used with our debug-paste tool
 */
@Data
public class ErrorManager {
    private final List<Entry> errorLog = new LinkedList<>();
    private final Map<GameFrame, List<Entry>> activeErrors = new HashMap<>();

    public void destroy() {
        errorLog.clear();
        activeErrors.clear();
    }

    public void newError(GameFrame gameFrame, Entry entry) {
        newError(entry);

        if (isError(gameFrame)) {
            activeErrors.get(gameFrame).add(entry);
        } else {
            activeErrors.put(gameFrame, new LinkedList<>(Collections.singletonList(entry)));
        }
    }

    public void removeError(Entry entry) {
        activeErrors.forEach((gameFrame, entries) -> entries.removeIf(savedEntry -> savedEntry.getType() == entry.getType()));
    }

    public void removeAllErrors(GameFrame gameFrame) {
        activeErrors.keySet().removeIf(savedGame -> savedGame.getGameName().equals(gameFrame.getGameName()));
    }

    public boolean isError(GameFrame gameFrame) {
        return activeErrors.containsKey(gameFrame);
    }

    public Entry newError(Entry entry) {
        return newError(entry, false);
    }

    public Entry newError(Entry entry, boolean writeError) {
        errorLog.add(entry);
        writeError(entry, writeError);

        return entry;
    }

    public void writeError(Entry entry, boolean stackTrace) {
        String langMessage = m(entry.getLanguageKey()).get();
        if (langMessage.equalsIgnoreCase(entry.getLanguageKey())) {
            langMessage = entry.getDefaultMessage();
        }

        Debug.warn(langMessage, true);
        if (stackTrace) {
            entry.getException().printStackTrace();
        }
    }

    @Data
    public static class Entry {
        private final Type type;
        private final Exception exception;
        private String languageKey;
        private String defaultMessage;

        public Entry(Type type, Exception exception) {
            this.type = type;
            this.exception = exception;
            this.languageKey = type.getLanguageKey();
            this.defaultMessage = type.getDefaultMessage();
        }
    }

    public static Entry newEntry(Type type, Exception exception) {
        return new Entry(type, exception);
    }

    public enum Type {
        GAME_CORE_ERROR("core.errors.game_core", "&cSomething went wrong with the GameCore instance. Please report this error log to GitHub or Discord!", Collections.emptyMap()),
        GAME_CONFIG_ERROR("core.errors.game_config", "&cGameConfiguration is wrong. Please report this error log to GitHub or Discord!", Collections.emptyMap()),
        GAME_SHOP_ERROR("core.errors.game_shop", "&cYour shop configuration is invalid. Please check validity of your current file.", Collections.emptyMap()),
        GAME_LOADING_ERROR("core.errors.game_loading", "&c&lOh, damn. &cLoading some arena file went wrong. &a&lPlease report this error log to GitHub or Discord!", new HashMap<>()),
        GAME_WORLD_DOES_NOT_EXISTS("core.errors.game_world_does_not_exists", "&cGame world for the game &e%game% &cdoes not exists!", new HashMap<>()),
        LOBBY_WORLD_DOES_NOT_EXISTS("core.errors.lobby_world_does_not_exists", "&cLobby world for the game &e%game% &cdoes not exists!", new HashMap<>()),
        UNKNOWN("core.errors.unknown", "&cUnknown error occurred. Error code printed to console, please report it to our GitHub or Discord!", Collections.emptyMap());

        private final Map<String, String> replaceable;
        private final String languageKey;
        private final String defaultMessage;

        Type(String languageKey, String defaultMessage, Map<String, String> replaceable) {
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
    }
}
