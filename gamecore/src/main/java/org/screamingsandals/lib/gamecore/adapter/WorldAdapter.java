package org.screamingsandals.lib.gamecore.adapter;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.Serializable;

@Data
public class WorldAdapter implements Serializable {
    private final String worldName;

    public static WorldAdapter create(World world) {
        return new WorldAdapter(world.getName());
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public boolean isWorldExists() {
        return getWorld() != null;
    }
}
