package org.screamingsandals.lib.gamecore.world;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.screamingsandals.lib.gamecore.core.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.core.adapter.WorldAdapter;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class BaseWorld {
    private LocationAdapter position1;
    private LocationAdapter position2;
    private LocationAdapter spawn;
    private WorldAdapter worldAdapter;
    private transient Map<Location, BlockData> originalBlocks = new HashMap<>();
    private transient Map<Location, BlockData> destroyedBlocks = new HashMap<>();
    private transient Map<Location, BlockData> placedBlocks = new HashMap<>();

    public boolean exists() {
        return position1.getWorld() != null;
    }

    public void putOriginalBlock(Location location, BlockState blockState) {
        originalBlocks.put(location, blockState.getBlockData());
    }

    public void putDestroyedBlock(Location location, BlockState blockState) {
        destroyedBlocks.put(location, blockState.getBlockData());
    }

    public void putPlacedBlock(Location location, BlockState blockState) {
        placedBlocks.put(location, blockState.getBlockData());
    }

    public boolean isLiquid(Material material) {
        return material == Material.WATER || material == Material.LAVA;
    }
}
