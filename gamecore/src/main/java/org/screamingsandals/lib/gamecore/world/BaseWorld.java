package org.screamingsandals.lib.gamecore.world;

import io.papermc.lib.PaperLib;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.adapter.WorldAdapter;
import org.screamingsandals.lib.gamecore.error.BaseError;
import org.screamingsandals.lib.gamecore.error.ErrorType;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class BaseWorld {
    private WorldAdapter worldAdapter;
    private LocationAdapter border1;
    private LocationAdapter border2;
    private LocationAdapter spawn;
    private transient Map<Location, BlockData> originalBlocks = new HashMap<>();
    private transient Map<Location, BlockData> destroyedBlocks = new HashMap<>();
    private transient Map<Location, BlockData> placedBlocks = new HashMap<>();

    public BaseWorld(String worldName) {
        this.worldAdapter = new WorldAdapter(worldName);
    }

    public boolean exists() {
        return border1.getWorld() != null;
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

    public void regen() {
        placedBlocks.keySet().forEach(location -> {
            getAndLoadChunkAsync(location);
            location.getBlock().setType(Material.AIR);
        });

        destroyedBlocks.forEach((location, blockData) -> {
            getAndLoadChunkAsync(location);
            location.getBlock().setBlockData(blockData);
        });

        placedBlocks.clear();
        destroyedBlocks.clear();
    }

    public static void getAndLoadChunkAsync(Location location) {
        final var asyncChunk = PaperLib.getChunkAtAsync(location, false);

        if (asyncChunk.isDone()) {
            try {
                final var chunk = asyncChunk.get();
                chunk.load();
            } catch (Exception e) {
                GameCore.getErrorManager().writeError(new BaseError(ErrorType.UNKNOWN, e), true); //todo
                final var nonAsyncChunk = location.getChunk();
                nonAsyncChunk.load();
            }
        }
    }
}
