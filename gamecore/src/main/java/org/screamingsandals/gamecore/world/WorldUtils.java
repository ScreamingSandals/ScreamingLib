package org.screamingsandals.gamecore.world;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Bed;

public class WorldUtils {
    public static boolean isBed(BlockState block) {
        return block.getBlockData() instanceof Bed;
    }

    public static boolean isBedHead(BlockState block) {
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
