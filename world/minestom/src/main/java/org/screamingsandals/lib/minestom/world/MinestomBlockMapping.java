package org.screamingsandals.lib.minestom.world;

import net.minestom.server.instance.block.Block;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.BlockMapping;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapping;

public class MinestomBlockMapping extends BlockMapping {

    public static void init() {
        BlockMapping.init(MinestomBlockMapping::new);
    }

    public MinestomBlockMapping() {
        converter
                .registerP2W(LocationHolder.class, location -> {
                    final var instanced = location.as(InstancedBlockPosition.class);
                    final var block = getBlock(instanced);
                    return new BlockHolder(location, MaterialMapping.resolve(block).orElseThrow());
                })
                .registerP2W(InstancedBlockPosition.class, position -> {
                    final var block = getBlock(position);
                    return new BlockHolder(LocationMapping.resolve(position).orElseThrow(), MaterialMapping.resolve(block).orElseThrow());
                })
                .registerW2P(Block.class, holder -> Block.valueOf(holder.getBlock().getPlatformName()));
    }

    @Override
    protected BlockHolder getBlockAt0(LocationHolder location) {
        return converter.convert(location);
    }

    @Override
    protected void setBlockAt0(LocationHolder location, MaterialHolder material) {
        final var position = location.as(InstancedBlockPosition.class);
        position.getInstance().setBlock(position, material.as(Block.class));
    }

    private Block getBlock(InstancedBlockPosition position) {
        return Block.fromStateId(position.getInstance().getBlockStateId(position));
    }
}
