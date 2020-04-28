package org.screamingsandals.lib.gamecore.error;

import lombok.Data;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.core.GameFrame;

import java.util.*;

import static org.screamingsandals.lib.lang.I.mpr;

public class ErrorManager {
    private final List<Entry> errorLog = new LinkedList<>();
    private final Map<GameFrame, List<Entry>> activeErrors = new HashMap<>();

    public void destroy() {
        errorLog.clear();
        activeErrors.clear();
    }

    public void addError(GameFrame gameFrame, Entry entry) {
        addError(entry);

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

    public void addError(Entry entry) {
        errorLog.add(entry);
        writeError(entry);
    }

    public void writeError(Entry entry) {
        String langMessage = mpr(entry.getLanguageKey()).get();
        if (langMessage == null) {
            langMessage = entry.getDefaultMessage();
        }

        Debug.warn(langMessage, true);
        entry.getException().printStackTrace();
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
        GAME_CORE_ERROR("errors.game_core", "&cSomething went wrong with the GameCore instance. Please report this error log to GitHub or Discord!"),
        GAME_SHOP_ERROR("errors.game_shop_error", "&cYour shop is not valid. Check configuration!"),
        GAME_LOADING_ERROR("errors.game_loading_error", "&cThere has been error while loading a game! Please report this error log to GitHub or Discord."),
        GAME_WORLD_DOES_NOT_EXISTS("errors.game_world_does_not_exists", "&cWorld for the game &e%game% &cdoes not exists!"),
        UNKNOWN("errors.unknown", "&cUnknown error occurred. Error code printed to console, please report it to our GitHub or Discord!");

        private final String languageKey;
        private final String defaultMessage;

        Type(String languageKey, String defaultMessage) {
            this.languageKey = languageKey;
            this.defaultMessage = defaultMessage;
        }

        public String getLanguageKey() {
            return languageKey;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }
    }
}
