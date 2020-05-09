package org.screamingsandals.lib.gamecore.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.gamecore.core.adapter.LocationAdapter;

@EqualsAndHashCode(callSuper = false)
@Data
public class GameWorld extends BaseWorld {
    private LocationAdapter spectatorSpawn;

    public static GameWorld get() {
        return new GameWorld();
    }
}
