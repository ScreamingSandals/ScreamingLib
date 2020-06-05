package org.screamingsandals.lib.gamecore.world.regeneration;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Bed;
import org.bukkit.material.Colorable;
import org.bukkit.material.Directional;
import org.bukkit.material.Lever;

import java.util.*;

//TODO
public class LegacyWorldRegeneration implements Regenerable {
    private final List<Location> builtBlocks = new ArrayList<>();
    private final List<Block> destroyedBlocks = new ArrayList<>();
    private final Map<Block, Block> brokenBeds = new HashMap<>();
    private final Map<Block, Byte> brokenBlockData = new HashMap<>();
    private final Map<Block, BlockFace> brokenBlockFace = new HashMap<>();
    private final Map<Block, Boolean> brokenBlockPower = new HashMap<>();
    private final Map<Block, Material> brokenBlockTypes = new HashMap<>();
    private final Map<Block, DyeColor> brokenBlockColors = new HashMap<>();

    @Override
    @SuppressWarnings("deprecation")
    public void regenerate() {
        for (var block : builtBlocks) {
            final var chunk = block.getChunk();
            if (!chunk.isLoaded()) {
                chunk.load();
            }
            block.getBlock().setType(Material.AIR);
        }

        builtBlocks.clear();

        for (var block : destroyedBlocks) {
            final var chunk = block.getChunk();
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
                final var data = block.getState().getData();
                if (data instanceof Directional) {
                    ((Directional) data).setFacingDirection(brokenBlockFace.get(block));
                    block.getState().setData(data);
                }
            }

            if (block.getState().getData() instanceof Lever) {
                final var attach = (Lever) block.getState().getData();
                final var supportState = block.getState();
                final var initialState = block.getState();
                attach.setPowered(brokenBlockPower.get(block));
                block.getState().setData(attach);

                supportState.setType(Material.AIR);
                supportState.update(true, false);
                initialState.update(true);
            } else {
                block.getState().update(true, true);
            }

            if (brokenBlockColors.containsKey(block) && block.getState() instanceof Colorable) {
                // Update bed color on 1.12.x
                final var state = block.getState();
                ((Colorable) state).setColor(brokenBlockColors.get(block));
                state.update(true, false);
            }
        }

        for (Map.Entry<Block, Block> entry : brokenBeds.entrySet()) {
            final var blockHead = entry.getKey();
            final var blockFeed = entry.getValue();
            var headState = blockHead.getState();
            var feedState = blockFeed.getState();

            headState.setType(brokenBlockTypes.get(blockHead));
            feedState.setType(brokenBlockTypes.get(blockFeed));
            headState.setRawData((byte) 0x0);
            feedState.setRawData((byte) 0x8);
            feedState.update(true, false);
            headState.update(true, false);

            final var bedHead = (Bed) headState.getData();
            bedHead.setHeadOfBed(true);
            bedHead.setFacingDirection(Objects.requireNonNull(blockHead.getFace(blockFeed)).getOppositeFace());

            final var bedFeed = (Bed) feedState.getData();
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

        destroyedBlocks.clear();
        brokenBlockData.clear();
        brokenBlockFace.clear();
        brokenBlockPower.clear();
        brokenBlockTypes.clear();
        brokenBlockColors.clear();
    }

    @Override
    public void registerBuiltBlock(Block block) {
        builtBlocks.add(block.getLocation());
    }

    @Override
    public void unregisterBuiltBlock(Block block) {
        builtBlocks.remove(block.getLocation());
    }

    @Override
    public void unregisterBuiltBlock(Location location) {
        builtBlocks.remove(location);
    }

    @Override
    public void registerDestroyedBlock(Block block) {
        destroyedBlocks.add(block);
    }

    @Override
    public void unregisterDestroyedBlock(Block block) {
        destroyedBlocks.remove(block);
    }

    @Override
    public void unregisterDestroyedBlock(Location location) {
        destroyedBlocks.remove(location.getBlock());
    }

    private void regenerateBeds(Map<Block, Block> blocks) {

    }
}
