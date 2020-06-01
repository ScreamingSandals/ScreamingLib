package org.screamingsandals.lib.gamecore.core.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.error.BaseError;
import org.screamingsandals.lib.gamecore.error.ErrorType;

import java.util.HashMap;
import java.util.Map;

@Data
public class GameConfig {
    private final Map<String, ValueHolder<?>> gameValues = new HashMap<>();

    public interface DefaultKeys {
        String TEAMS_ENABLED = "teams";
        String STORES_ENABLED = "stores";
        String RESOURCES_ENABLED = "resources";
        String SPECTATORS_ENABLED = "spectators";
        String START_TIME = "start_time";
        String GAME_TIME = "game_time";
        String DEATHMATCH_TIME = "deathmatch_time";
        String END_GAME_TIME = "end_game_time";
    }

    public void registerValue(ValueHolder<?> value) {
        gameValues.putIfAbsent(value.getKey(), value);
    }

    public void unregisterValue(String key) {
        gameValues.remove(key);
    }

    public void unregisterValue(ValueHolder<?> value) {
        unregisterValue(value.getKey());
    }

    public void replaceValue(ValueHolder<?> value) {
        final var key = value.getKey();

        gameValues.remove(key);
        gameValues.put(key, value);
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

    public Map<String, ValueHolder<?>> getGameValues() {
        return new HashMap<>(gameValues);
    }

    @Data
    @AllArgsConstructor
    public static class ValueHolder<T> {
        private String key;
        private T value;
        private GameValue gameValue;

        public static <T> ValueHolder<T> get(String key, T value, GameValue gameValue) {
            return new ValueHolder<>(key, value, gameValue);
        }

        public boolean getBooleanValue() {
            return (boolean) value;
        }

        public int getIntValue() {
            //due to GSON
            if (value instanceof Double) {
                return ((Double) value).intValue();
            }
            return (int) value;
        }

        public String getStringValue() {
            return (String) value;
        }

        public double getDoubleValue() {
            return (double) value;
        }
    }
}
