package org.screamingsandals.lib.gamecore.area;

import lombok.Getter;
import org.screamingsandals.lib.gamecore.elements.GameElementValue;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

@Getter
public class GameArea extends Area {
    private final GameElementValue<LocationHolder> spectatorSpawn = new GameElementValue<>();

    public GameArea(UUID uuid) {
        super(uuid);
    }
}
