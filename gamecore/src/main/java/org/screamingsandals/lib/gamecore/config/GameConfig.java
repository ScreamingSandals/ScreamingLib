package org.screamingsandals.lib.gamecore.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.error.BaseError;
import org.screamingsandals.lib.gamecore.error.ErrorType;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@ToString(exclude = "gameFrame")
public class GameConfig {
    private transient GameFrame gameFrame;
    private final Map<String, ValueHolder<?>> gameValues = new HashMap<>();

    public GameConfig(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void buildDefaults() {
        gameValues.put(DefaultKeys.GAME_TIME, ValueHolder.get(DefaultKeys.GAME_TIME, 3600));
        gameValues.put(DefaultKeys.LOBBY_TIME, ValueHolder.get(DefaultKeys.LOBBY_TIME, 60));
        gameValues.put(DefaultKeys.START_TIME, ValueHolder.get(DefaultKeys.START_TIME, 10));
        gameValues.put(DefaultKeys.DEATHMATCH_TIME, ValueHolder.get(DefaultKeys.DEATHMATCH_TIME, 600));
        gameValues.put(DefaultKeys.END_GAME_TIME, ValueHolder.get(DefaultKeys.END_GAME_TIME, 5));

        gameFrame.setGameTime(getValue(DefaultKeys.GAME_TIME));
        gameFrame.setLobbyTime(getValue(DefaultKeys.LOBBY_TIME));
        gameFrame.setStartTime(getValue(DefaultKeys.START_TIME));
        gameFrame.setDeathmatchTime(getValue(DefaultKeys.DEATHMATCH_TIME));
        gameFrame.setEndTime(getValue(DefaultKeys.END_GAME_TIME));
    }

    public interface DefaultKeys {
        String LOBBY_TIME = "lobby_time";
        String START_TIME = "start_time";
        String GAME_TIME = "game_time";
        String DEATHMATCH_TIME = "deathmatch_time";
        String END_GAME_TIME = "end_game_time";
    }

    public void registerValue(String key, ValueHolder<?> value) {
        gameValues.putIfAbsent(key, value);
    }

    public void unregisterValue(String key) {
        gameValues.remove(key);
    }

    public void replaceValue(String key, ValueHolder<?> valueHolder) {
        gameValues.remove(key);
        gameValues.put(key, valueHolder);
    }

    public ValueHolder<?> getValueHolder(String key) {
        return gameValues.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        if (!gameValues.containsKey(key)) {
            Debug.warn("It does not exists");
            return null;
        }
        try {
            return (T) gameValues.get(key).getValue();
        } catch (Exception e) {
            GameCore.getErrorManager().newError(new BaseError(ErrorType.GAME_CONFIG_ERROR, e), true);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, T def) {
        if (!gameValues.containsKey(key)) {
            Debug.warn("It does not exists");
            return def;
        }
        try {
            return (T) gameValues.get(key).getValue();
        } catch (Exception e) {
            GameCore.getErrorManager().newError(new BaseError(ErrorType.GAME_CONFIG_ERROR, e), true);
        }
        return def;
    }

    public Map<String, Object> getGameValues() {
        return new HashMap<>(gameValues);
    }

    @Data
    @AllArgsConstructor
    public static class ValueHolder<T> {
        private String key;
        private T value;

        public static <T> ValueHolder<T> get(String key, T value) {
            return new ValueHolder<>(key, value);
        }
    }

    //TODO
    public enum PerArenaValue {
        TRUE,
        FALSE,
        INHERIT;
    }
}
