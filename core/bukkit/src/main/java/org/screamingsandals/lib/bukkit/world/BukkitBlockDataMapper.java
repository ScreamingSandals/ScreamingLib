package org.screamingsandals.lib.bukkit.world;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BukkitBlockDataMapper extends BlockDataMapper {
    public BukkitBlockDataMapper() {
        converter
                .registerP2W(BlockHolder.class, parent -> {
                    if (Version.isVersion(1, 13)) {
                        final var data = parent.as(Block.class).getBlockData();
                        final var holder = new BlockDataHolder(MaterialHolder.of(data.getMaterial()), getDataFromString(data.getAsString()), parent);

                        if (!holder.getType().getPlatformName().equalsIgnoreCase(parent.getType().getPlatformName())) {
                            parent.setType(holder.getType());
                        }

                        return holder;
                    } else {
                        final var data = parent.as(Block.class).getState().getData();
                        final var holder = new BlockDataHolder(MaterialHolder.of(data.getItemType() + ":" + data.getData()), LegacyBlockDataConverter.convertMaterialData(data), parent);

                        if (!holder.getType().getPlatformName().equalsIgnoreCase(parent.getType().getPlatformName())) {
                            parent.setType(holder.getType());
                        }

                        return holder;
                    }
                })
                .registerP2W(Location.class, position ->
                        resolve(BlockMapper.resolve(position)).orElseThrow())
                .registerP2W(Block.class, block ->
                        resolve(BlockMapper.resolve(block)).orElseThrow())
                .registerP2W(LocationHolder.class, location ->
                        resolve(BlockMapper.resolve(location).orElseThrow()).orElseThrow());

        if (Version.isVersion(1, 13)) {
            converter
                    .registerP2W(BlockData.class, blockData ->
                            new BlockDataHolder(MaterialHolder.of(blockData.getMaterial()), getDataFromString(blockData.getAsString()), null)
                    )
                    .registerW2P(BlockData.class, holder ->
                            Bukkit.createBlockData(getDataFromMap(holder.getType(), holder.getData())));
        } else {
            converter
                    .registerP2W(MaterialData.class, data ->
                            new BlockDataHolder(MaterialHolder.of(data.getItemType() + ":" + data.getData()), LegacyBlockDataConverter.convertMaterialData(data), null)
                    )
                    .registerW2P(MaterialData.class, holder -> LegacyBlockDataConverter.asMaterialData(holder.getType().as(Material.class), holder.getType().getDurability(), holder.getData()));
        }
    }

    @Override
    protected Optional<BlockDataHolder> getBlockDataAt0(LocationHolder location) {
        return resolve(location);
    }

    @Override
    protected void setBlockDataAt0(LocationHolder location, BlockDataHolder blockData) {
        final var block = location.as(Location.class).getBlock();

        if (Version.isVersion(1, 13)) {
            block.setBlockData(blockData.as(BlockData.class));
        } else {
            final var state = block.getState();
            state.setData(blockData.as(MaterialData.class));
            state.update();
        }
    }

    private Map<String, Object> getDataFromString(String data) {
        Preconditions.checkNotNull(data, "Data cannot be null!");
        if (data.contains("[") && data.contains("]")) {
            final var values = data.substring(data.indexOf("[") + 1, data.lastIndexOf("]"));
            if (values.isEmpty()) {
                return Map.of();
            }
            return Arrays.stream(values.split(","))
                    .map(next -> next.split("="))
                    .collect(Collectors.toMap(next -> next[0], next1 -> next1[1]));
        }
        return Map.of();
    }

    private String getDataFromMap(MaterialHolder material, Map<String, Object> data) {
        final var builder = new StringBuilder("minecraft:" + material.getPlatformName().toLowerCase());
        if (!data.isEmpty()) {
            builder.append('[');
            builder.append(data
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining(",")));
            builder.append(']');
        }

        return builder.toString();
    }
}
