package org.screamingsandals.lib.bukkit.gamecore.region;

import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.*;
import org.screamingsandals.lib.gamecore.region.Region;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegacyRegion implements Region {
    private final List<Location> builtBlocks = new ArrayList<>();
    private final List<Block> brokenBlocks = new ArrayList<>();
    private final HashMap<Block, Block> brokenBeds = new HashMap<>();
    private final HashMap<Block, Byte> brokenBlockData = new HashMap<>();
    private final HashMap<Block, BlockFace> brokenBlockFace = new HashMap<>();
    private final HashMap<Block, Boolean> brokenBlockPower = new HashMap<>();
    private final HashMap<Block, Material> brokenBlockTypes = new HashMap<>();
    private final HashMap<Block, DyeColor> brokenBlockColors = new HashMap<>();

    @Override
    public boolean isBlockAddedDuringGame(LocationHolder loc) {
        return builtBlocks.contains(loc.as(Location.class));
    }

    @Override
    public void putOriginalBlock(LocationHolder loc, BlockStateHolder blockS) {
        var bloc = loc.as(Location.class);
        var block = blockS.as(BlockState.class);
    	if (!block.getType().name().equals("BED_BLOCK")) {
    		brokenBlocks.add(bloc.getBlock());
    	}

        if (block.getData() instanceof Directional) {
            brokenBlockFace.put(bloc.getBlock(), ((Directional) block.getData()).getFacing());
        }

        brokenBlockTypes.put(bloc.getBlock(), block.getType());
        brokenBlockData.put(bloc.getBlock(), block.getData().getData());

        if (block.getData() instanceof Redstone) {
            brokenBlockPower.put(bloc.getBlock(), ((Redstone) block.getData()).isPowered());
        }

        if (block instanceof Colorable) {
            // Save bed color on 1.12.x
            brokenBlockColors.put(bloc.getBlock(), ((Colorable) block).getColor());
        }
        
        if (isBedHead(blockS)) {
        	brokenBeds.put(bloc.getBlock(), getBedNeighbor(BlockMapper.getBlockAt(loc)).as(Block.class));
        }
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
    public void regen() {
        for (Location block : builtBlocks) {
            Chunk chunk = block.getChunk();
            if (!chunk.isLoaded()) {
                chunk.load();
            }
            block.getBlock().setType(Material.AIR);
        }
        builtBlocks.clear();

        for (Block block : brokenBlocks) {
            Chunk chunk = block.getChunk();
            if (!chunk.isLoaded()) {
                chunk.load();
            }
            block.setType(brokenBlockTypes.get(block));
            try {
                // The method is no longer in API, but in legacy versions exists
                Block.class.getMethod("setData", byte.class).invoke(block, brokenBlockData.get(block));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (brokenBlockFace.containsKey(block)) {
                MaterialData data = block.getState().getData();
                if (data instanceof Directional) {
                    ((Directional) data).setFacingDirection(brokenBlockFace.get(block));
                    block.getState().setData(data);
                }
            }

            if (block.getState().getData() instanceof Lever) {
                Lever attach = (Lever) block.getState().getData();
                BlockState supportState = block.getState();
                BlockState initalState = block.getState();
                attach.setPowered(brokenBlockPower.get(block));
                block.getState().setData(attach);

                supportState.setType(Material.AIR);
                supportState.update(true, false);
                initalState.update(true);
            } else {
                block.getState().update(true, true);
            }

            if (brokenBlockColors.containsKey(block) && block.getState() instanceof Colorable) {
                // Update bed color on 1.12.x
                BlockState state = block.getState();
                ((Colorable) state).setColor(brokenBlockColors.get(block));
                state.update(true, false);
            }
        }
        
        for (Map.Entry<Block, Block> entry : brokenBeds.entrySet()) {
            Block blockHead = entry.getKey();
            Block blockFeed = entry.getValue();
            BlockState headState = blockHead.getState();
            BlockState feedState = blockFeed.getState();

            headState.setType(brokenBlockTypes.get(blockHead));
            feedState.setType(brokenBlockTypes.get(blockFeed));
            headState.setRawData((byte) 0x0);
            feedState.setRawData((byte) 0x8);
            feedState.update(true, false);
            headState.update(true, false);

            Bed bedHead = (Bed) headState.getData();
            bedHead.setHeadOfBed(true);
            bedHead.setFacingDirection(blockHead.getFace(blockFeed).getOppositeFace());

            Bed bedFeed = (Bed) feedState.getData();
            bedFeed.setHeadOfBed(false);
            bedFeed.setFacingDirection(blockFeed.getFace(blockHead));

            feedState.update(true, false);
            headState.update(true, true);
            headState = blockHead.getState();
            feedState = blockFeed.getState();

            if (brokenBlockColors.containsKey(blockFeed) && feedState instanceof Colorable) {
                // Update bed color on 1.12.x
            	((Colorable) feedState).setColor(brokenBlockColors.get(blockFeed));
                feedState.update(true, false);
            }

            if (brokenBlockColors.containsKey(blockHead) && headState instanceof Colorable) {
                // Update bed color on 1.12.x
            	((Colorable) headState).setColor(brokenBlockColors.get(blockHead));
                headState.update(true, true);
            }
        }
        brokenBeds.clear();
        
        brokenBlocks.clear();
        brokenBlockData.clear();
        brokenBlockFace.clear();
        brokenBlockPower.clear();
        brokenBlockTypes.clear();
        brokenBlockColors.clear();
    }

    @Override
    public boolean isLiquid(MaterialHolder material) {
        return material.is("WATER", "LAVA", "STATIONARY_WATER", "STATIONARY_LAVA");
    }

    @Override
    public boolean isBedBlock(BlockStateHolder block) {
        return block.as(BlockState.class).getData() instanceof Bed;
    }

    @Override
    public boolean isBedHead(BlockStateHolder block) {
        return isBedBlock(block) && ((Bed) block.as(BlockState.class).getData()).isHeadOfBed();
    }

    @Override
    public BlockHolder getBedNeighbor(BlockHolder head) {
        return BlockMapper.wrapBlock(LegacyBedUtils.getBedNeighbor(head.as(Block.class)));
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
        for (Block block : brokenBlocks) {
            if (chunk.equals(block.getChunk())) {
                return true;
            }
        }
        return false;
    }

}
