package org.screamingsandals.lib.minestom.world;

import net.minestom.server.data.Data;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class MinestomBlockDataMapping extends BlockDataMapping {

    public static void init() {
        BlockDataMapping.init(MinestomBlockDataMapping::new);
    }

    public MinestomBlockDataMapping() {
        converter
                .registerP2W(BlockHolder.class, parent -> {
                    final var position = parent.getLocation().as(InstancedBlockPosition.class);
                    final var data = position.getInstance().getBlockData(position);
                    if (data == null) {
                        return new BlockDataHolder(parent.getType(), Map.of(), parent);
                    }

                    final var map = new HashMap<String, Object>();
                    data.getKeys().forEach(key -> map.put(key, data.get(key)));
                    return new BlockDataHolder(parent.getType(), map, parent);
                })
                .registerP2W(InstancedBlockPosition.class, position ->
                        resolve(BlockMapping.resolve(position)).orElseThrow())
                .registerP2W(InstancedPosition.class, position ->
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
    protected Optional<BlockDataHolder> getBlockDataAt0(LocationHolder location) {
        return resolve(location);
    }

    @Override
    protected void setBlockDataAt0(LocationHolder location, BlockDataHolder blockData) {
        final var position = location.as(InstancedBlockPosition.class);
        position.getInstance().setBlockData(position, blockData.as(Data.class));
    }
}
