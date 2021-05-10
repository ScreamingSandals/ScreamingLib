package org.screamingsandals.lib.bukkit.gamecore.region;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Bed.Part;
import org.screamingsandals.lib.gamecore.region.Region;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: improve world module and make platform independent
public class FlatteningRegion implements Region {
    private final List<Location> builtBlocks = new ArrayList<>();
    private final Map<Location, BlockData> brokenOriginalBlocks = new HashMap<>();

    @Override
    public boolean isBlockAddedDuringGame(LocationHolder loc) {
        return builtBlocks.contains(loc.as(Location.class));
    }

    @Override
    public void putOriginalBlock(LocationHolder loc, BlockStateHolder block) {
        brokenOriginalBlocks.put(loc.as(Location.class), block.getBlockData().as(BlockData.class));
    }

    @Override
    public void addBuiltDuringGame(LocationHolder loc) {
        builtBlocks.add(loc.as(Location.class));
    }

    @Override
    public void removeBlockBuiltDuringGame(LocationHolder loc) {
        builtBlocks.remove(loc.as(Location.class));
    }

    @Override
    public boolean isLiquid(MaterialHolder material) {
        return material.is("WATER", "LAVA");
    }

    @Override
    public boolean isBedBlock(BlockStateHolder block) {
        return block.getBlockData().as(BlockData.class) instanceof Bed;
    }

    @Override
    public void regen() {
        for (var block : builtBlocks) {
            var chunk = block.getChunk();
            if (!chunk.isLoaded()) {
                chunk.load();
            }
            block.getBlock().setType(Material.AIR);
        }
        builtBlocks.clear();
        for (var block : brokenOriginalBlocks.entrySet()) {
            var chunk = block.getKey().getChunk();
            if (!chunk.isLoaded()) {
                chunk.load();
            }
            block.getKey().getBlock().setBlockData(block.getValue());
        }
        brokenOriginalBlocks.clear();
    }

    @Override
    public boolean isBedHead(BlockStateHolder block) {
        return isBedBlock(block) && ((Bed) block.getBlockData()).getPart() == Part.HEAD;
    }

    @Override
    public BlockHolder getBedNeighbor(BlockHolder head) {
        return BlockMapper.wrapBlock(FlatteningBedUtils.getBedNeighbor(head.as(Block.class)));
    }

    //@Override
    public boolean isChunkUsed(Chunk chunk) {
        if (chunk == null) {
            return false;
        }
        for (Location loc : builtBlocks) {
            if (chunk.equals(loc.getChunk())) {
                return true;
            }
        }
        for (Location loc : brokenOriginalBlocks.keySet()) {
            if (chunk.equals(loc.getChunk())) {
                return true;
            }
        }
        return false;
    }
}
