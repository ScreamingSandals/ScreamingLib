package org.screamingsandals.lib.gamecore.world.regeneration;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Data
public class WorldRegeneration implements Regenerable {
    private final Map<Location, BlockState> destroyedBlocks = new HashMap<>();
    private final Map<Location, BlockState> builtBlocks = new HashMap<>();
    private final Map<Location, BlockState> interactedBlock = new HashMap<>();

    @Override
    public void regenerate() {
        final var toDestroyLater = new LinkedList<Location>();
        final var toRegenerateLater = new HashMap<Location, BlockState>();

        if (!builtBlocks.isEmpty()) {
            builtBlocks.forEach(((location, blockState) -> {
                if (Regenerable.loadChunkAsync(location)) {
                    Regenerable.destroyBlock(location);
                } else {
                    toDestroyLater.add(location);
                }
            }));

            builtBlocks.clear();
        }

        if (!destroyedBlocks.isEmpty()) {
            destroyedBlocks.forEach(((location, blockState) -> {
                if (Regenerable.loadChunkAsync(location)) {
                    Regenerable.changeState(location, blockState);
                } else {
                    toRegenerateLater.put(location, blockState);
                }
            }));

            destroyedBlocks.clear();
        }

        if (!interactedBlock.isEmpty()) {
            interactedBlock.forEach(((location, blockState) -> {
                if (Regenerable.loadChunkAsync(location)) {
                    Regenerable.changeState(location, blockState);
                } else {
                    toRegenerateLater.put(location, blockState);
                }
            }));

            interactedBlock.clear();
        }

        if (!toDestroyLater.isEmpty()) {
            toDestroyLater.forEach(Regenerable::destroyBlock);
            toDestroyLater.clear();
        }

        if (!toRegenerateLater.isEmpty()) {
            toRegenerateLater.forEach(Regenerable::changeState);
            toRegenerateLater.clear();
        }
    }

    @Override
    public void registerBuiltBlock(Block block) {
        builtBlocks.putIfAbsent(block.getLocation(), block.getState());
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
        destroyedBlocks.putIfAbsent(block.getLocation(), block.getState());
    }

    @Override
    public void unregisterDestroyedBlock(Block block) {
        destroyedBlocks.remove(block.getLocation());
    }

    @Override
    public void unregisterDestroyedBlock(Location location) {
        destroyedBlocks.remove(location);
    }
}
