package org.screamingsandals.lib.gamecore.area;

import lombok.Getter;
import org.screamingsandals.lib.gamecore.elements.GameElementValue;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

@Getter
public class LobbyArea extends Area {
    private final GameElementValue<LocationHolder> lobbySpawn = new GameElementValue<>();

    public LobbyArea(UUID uuid) {
        super(uuid);
    }
}
