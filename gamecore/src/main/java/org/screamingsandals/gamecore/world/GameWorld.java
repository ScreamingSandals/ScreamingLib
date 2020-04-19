package org.screamingsandals.gamecore.world;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.screamingsandals.gamecore.core.adapter.LocationAdapter;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class GameWorld {
    private LocationAdapter position1;
    private LocationAdapter position2;
    private LocationAdapter spawn;
    private LocationAdapter spectatorSpawn;
    private transient Map<Location, BlockData> originalBlocks = new HashMap<>();
    private transient Map<Location, BlockData> destroyedBlocks = new HashMap<>();
    private transient Map<Location, BlockData> placedBlocks = new HashMap<>();

    public boolean worldExists() {
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

    public boolean isBed(BlockState block) {
        return block.getBlockData() instanceof Bed;
    }

    public boolean isBedHead(BlockState block) {
        return isBed(block) && ((Bed) block.getBlockData()).getPart() == Bed.Part.HEAD;
    }

    public static Block getBedNeighbor(Block head) {
        if (!(head.getBlockData() instanceof Bed)) {
            return null;
        }

        if (isBedBlock(head.getRelative(BlockFace.EAST))) {
            return head.getRelative(BlockFace.EAST);
        } else if (isBedBlock(head.getRelative(BlockFace.WEST))) {
            return head.getRelative(BlockFace.WEST);
        } else if (isBedBlock(head.getRelative(BlockFace.SOUTH))) {
            return head.getRelative(BlockFace.SOUTH);
        } else {
            return head.getRelative(BlockFace.NORTH);
        }
    }

    public static boolean isBedBlock(Block block) {
        if (block == null) {
            return false;
        }

        return block.getBlockData() instanceof Bed;
    }
}
