package org.screamingsandals.lib.gamecore.area;

import lombok.Getter;
import org.screamingsandals.lib.gamecore.elements.GameElement;
import org.screamingsandals.lib.gamecore.elements.GameElementValue;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

@Getter
public abstract class Area extends GameElement {
    private final GameElementValue<LocationHolder> pos1 = new GameElementValue<>();
    private final GameElementValue<LocationHolder> pos2 = new GameElementValue<>();

    public Area(UUID uuid) {
        super(uuid);
    }
}
