package org.screamingsandals.lib.minestom.world;

import net.minestom.server.data.Data;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.PlatformMapping;
import org.screamingsandals.lib.world.*;

import java.util.Optional;

@PlatformMapping(platform = PlatformType.MINESTOM)
public class MinestomBlockDataMapping extends BlockDataMapping {

    public static void init() {
        BlockDataMapping.init(MinestomBlockDataMapping::new);
    }

    public MinestomBlockDataMapping() {
        converter
                .registerP2W(BlockHolder.class, parent -> {
                    final var position = parent.getLocation().as(InstancedBlockPosition.class);
                    final var toReturn = new BlockDataHolder(parent);
                    final var data = position.getInstance().getBlockData(position);
                    if (data == null) {
                        return toReturn;
                    }

                    data.getKeys().forEach(key -> toReturn.addData(key, data.get(key)));
                    return toReturn;
                })
                .registerP2W(InstancedBlockPosition.class, position ->
                        resolve(BlockMapping.resolve(position)).orElseThrow())
                .registerP2W(LocationHolder.class, location ->
                        resolve(BlockMapping.resolve(location).orElseThrow()).orElseThrow())
                .registerW2P(Data.class, holder -> {
                    final var position = holder.getParent().getLocation().as(InstancedBlockPosition.class);
                    final var blockData = position.getInstance().getBlockData(position);
                    if (blockData == null) {
                        return null;
                    }

                    holder.getData().forEach(blockData::set);
                    return blockData;
                });
    }

    @Override
    protected Optional<BlockDataHolder> getBlockStateAt0(LocationHolder location) {
        return resolve(location);
    }

    @Override
    protected void setBlockStateAt0(LocationHolder location, BlockDataHolder dataHolder) {
        final var position = location.as(InstancedBlockPosition.class);
        position.getInstance().setBlockData(position, dataHolder.as(Data.class));
    }
}
