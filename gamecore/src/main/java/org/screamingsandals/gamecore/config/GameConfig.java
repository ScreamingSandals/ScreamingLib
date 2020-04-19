package org.screamingsandals.gamecore.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GameConfig {
    private Map<String, Object> GAME_CONFIG = new HashMap<>();

    public void addDefaults() {

    }
}
