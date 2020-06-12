package org.screamingsandals.lib.gamecore.adapter;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.World;
import org.screamingsandals.lib.debug.Debug;

import java.io.Serializable;

/**
 * As Bukkit's {@link Location} is no-go for serializations, we need to implement our own adapter.
 * This is fairly simple, just create new LocationAdapter with the {@link Location} values
 * and that's it.
 *
 * This uses {@link WorldAdapter as well.}
 */

@Data
public class LocationAdapter implements Serializable {
    private WorldAdapter worldAdapter;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private transient Location location;

    public LocationAdapter(Location location) {
        this(new WorldAdapter(location.getWorld().getName()), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
    }

    public LocationAdapter(WorldAdapter worldAdapter, double x, double y, double z, float pitch, float yaw) {
        this.worldAdapter =worldAdapter;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;

        location = constructLocation();
    }

    public World getWorld() {
        return worldAdapter.getWorld();
    }

    public boolean isLocationValid() {
        return location != null;
    }

    public Location getLocation() {
        if (location == null) {
            location = constructLocation();
        }

        return location;
    }

    private Location constructLocation() {
        if (worldAdapter == null) {
            Debug.warn("World does not exists!", true);
            return null;
        }

        if (!worldAdapter.isWorldExists()) {
            Debug.warn("World " + worldAdapter.getWorldName() + " is null!", true);
            return null;
        }

        return new Location(worldAdapter.getWorld(), x, y, z, yaw, pitch);
    }
}
