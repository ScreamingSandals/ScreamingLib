package org.screamingsandals.lib.gamecore.world.regeneration;

import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.error.BaseError;
import org.screamingsandals.lib.gamecore.error.ErrorType;

public interface Regenerable {

    void regenerate();

    void registerBuiltBlock(Block block);

    void unregisterBuiltBlock(Block block);

    void unregisterBuiltBlock(Location location);

    void registerDestroyedBlock(Block block);

    void unregisterDestroyedBlock(Block block);

    void unregisterDestroyedBlock(Location location);

    static boolean isLiquid(Material material) {
        return material == Material.WATER || material == Material.LAVA;
    }

    static boolean loadChunkAsync(Location location) {
        try {
            final var asyncChunk = PaperLib.getChunkAtAsync(location, false);
            if (asyncChunk.isDone()) {
                final var chunk = asyncChunk.get();
                chunk.load();
                return true;
            }
            return false;
        } catch (Exception e) {
            GameCore.getErrorManager().writeError(new BaseError(ErrorType.UNKNOWN, e), true);
            final var nonAsyncChunk = location.getChunk();
            nonAsyncChunk.load();
            return true;
        }
    }

    static void destroyBlock(Location location) {
        location.getBlock().setType(Material.AIR);
    }

    static void changeState(Location location, BlockState blockState) {
        location.getBlock().setBlockData(blockState.getBlockData());
    }
}
