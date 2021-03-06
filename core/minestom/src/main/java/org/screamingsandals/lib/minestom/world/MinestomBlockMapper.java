package org.screamingsandals.lib.minestom.world;

import net.minestom.server.instance.block.Block;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.*;

@Service(dependsOn = {
        MinestomLocationMapper.class
})
public class MinestomBlockMapper extends BlockMapper {

    public static void init() {
        BlockMapper.init(MinestomBlockMapper::new);
    }

    public MinestomBlockMapper() {
        converter
                .registerP2W(LocationHolder.class, location -> {
                    final var instanced = location.as(InstancedBlockPosition.class);
                    final var block = getBlock(instanced);
                    final var holder = new BlockHolder(location,
                            MaterialMapping.resolve(block).orElseThrow());
                    holder.setBlockData(BlockDataMapper.resolve(holder).orElseThrow());

                    return holder;
                })
                .registerP2W(InstancedBlockPosition.class, position -> {
                    final var block = getBlock(position);
                    final var holder = new BlockHolder(LocationMapper.resolve(position).orElseThrow(),
                            MaterialMapping.resolve(block).orElseThrow());
                    BlockDataMapper.resolve(holder).ifPresent(holder::setBlockData);

                    return holder;
                })
                .registerW2P(Block.class, holder -> Block.valueOf(holder.getType().getPlatformName()));
    }

    @Override
    protected BlockHolder getBlockAt0(LocationHolder location) {
        return converter.convert(location);
    }

    @Override
    protected void setBlockAt0(LocationHolder location, MaterialHolder material) {
        final var position = location.as(InstancedBlockPosition.class);
        position.getInstance().setBlock(position, material.as(Block.class));
        position.getInstance().getBlockData(position);
    }

    @Override
    protected void breakNaturally0(LocationHolder location) {
        // TODO
    }

    private Block getBlock(InstancedBlockPosition position) {
        return Block.fromStateId(position.getInstance().getBlockStateId(position));
    }
}
