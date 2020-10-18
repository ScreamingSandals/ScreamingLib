package org.screamingsandals.lib.lang.registry;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class PlayerRegistry {
    private final Map<UUID, String> players = new HashMap<>();

    public void register(UUID uuid, String languageCode) {
        players.putIfAbsent(uuid, languageCode);
    }

    public void remove(UUID uuid) {
        players.remove(uuid);
    }
}
