package org.screamingsandals.gamecore.core.adapter;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.Serializable;

@Data
public class WorldAdapter implements Serializable {
    private String worldName;

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public boolean isWorldExists() {
        return getWorld() != null;
    }
}
