package org.screamingsandals.gamecore.core.adapter;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.World;
import org.screamingsandals.lib.debug.Debug;

import java.io.Serializable;

@Data
public class LocationAdapter implements Serializable {
    private WorldAdapter worldAdapter;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private transient Location location;

    public LocationAdapter() {
        location = constructLocation();
    }

    public World getWorld() {
        return worldAdapter.getWorld();
    }

    public boolean isLocationValid() {
        return location != null;
    }

    private Location constructLocation() {
        if (worldAdapter.isWorldExists()) {
            Debug.warn("World " + worldAdapter.getWorldName() + " is null!", true);
            return null;
        }

        return new Location(worldAdapter.getWorld(), x, y, z, pitch, yaw);
    }
}
