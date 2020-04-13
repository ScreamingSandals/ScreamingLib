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

    public Location getBukkitLocation() {
        if (worldAdapter.isWorldExists()) {
            Debug.warn("World " + worldAdapter.getWorldName() + " is null!");
            return null;
        }

        return new Location(worldAdapter.getWorld(), x, y, z, pitch, yaw);
    }

    public World getWorld() {
        return worldAdapter.getWorld();
    }

    public boolean isLocationValid() {
        Location location = getBukkitLocation();
        return location != null;
    }
}
