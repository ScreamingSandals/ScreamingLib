package org.screamingsandals.lib.gamecore.world;

import lombok.Data;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.adapter.WorldAdapter;
import org.screamingsandals.lib.gamecore.world.regeneration.Regenerable;

@Data
public abstract class BaseWorld {
    private WorldAdapter worldAdapter;
    private LocationAdapter border1;
    private LocationAdapter border2;
    private LocationAdapter spawn;
    protected transient Regenerable regenerator;

    public BaseWorld(String worldName) {
        this.worldAdapter = new WorldAdapter(worldName);
        regenerator = GameCore.getRegenerator();
    }

    public boolean exists() {
        return border1.getWorld() != null;
    }

    public void regenerate() {
        if (regenerator == null) {
            return;
        }

        regenerator.regenerate();
    }
}
