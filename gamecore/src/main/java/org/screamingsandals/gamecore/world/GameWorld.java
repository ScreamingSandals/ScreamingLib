package org.screamingsandals.gamecore.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.gamecore.core.adapter.LocationAdapter;

@EqualsAndHashCode(callSuper = false)
@Data
public class GameWorld extends BaseWorld {
    private LocationAdapter spectatorSpawn;
}
