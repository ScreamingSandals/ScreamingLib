package org.screamingsandals.lib.bukkit.world;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class BukkitBlockDataMapping extends BlockDataMapping {
    private final static Pattern MAPPING_PATTERN = Pattern.compile(
            "(?:(?<namespace>[A-Za-z][A-Za-z0-9_.\\-]*):)?(?<material>[A-Za-z][A-Za-z0-9_.\\-/ ]+)(\\[(?<blockState>.+)])?");

    public static void init() {
        BlockDataMapping.init(BukkitBlockDataMapping::new);
    }

    public BukkitBlockDataMapping() {
        converter
                .registerP2W(BlockHolder.class, parent -> {
                    final var position = parent.getLocation().as(Location.class);
                    final var data = position.getBlock().getBlockData();
                    final var holder = new BlockDataHolder(parent.getType(), getDataFromString(data.getAsString()), parent);

                    if (!holder.getType().getPlatformName().equalsIgnoreCase(parent.getType().getPlatformName())) {
                        parent.setType(holder.getType());
                    }

                    return holder;
                })
                .registerP2W(Location.class, position ->
                        resolve(BlockMapping.resolve(position)).orElseThrow())
                .registerP2W(Block.class, block ->
                        resolve(BlockMapping.resolve(block)).orElseThrow())
                .registerP2W(LocationHolder.class, location ->
                        resolve(BlockMapping.resolve(location).orElseThrow()).orElseThrow())
                .registerW2P(BlockData.class, holder ->
                        Bukkit.createBlockData(getDataFromMap(holder.getType(), holder.getData())));
    }

    @Override
    protected Optional<BlockDataHolder> getBlockDataAt0(LocationHolder location) {
        return resolve(location);
    }

    @Override
    protected void setBlockDataAt0(LocationHolder location, BlockDataHolder blockData) {
        final var resolved = BlockMapping.resolve(location);
        if (resolved.isEmpty()) {
            return;
        }

        resolved.get().setBlockData(blockData);
    }

    private Map<String, Object> getDataFromString(String data) {
        Preconditions.checkNotNull(data, "Data cannot be null!");
        final var matcher = MAPPING_PATTERN.matcher(data);
        if (matcher.group("material") != null) {
            final var values = matcher.group("blockState");
            if (values == null || values.isEmpty()) {
                return Map.of();
            }
            return Arrays.stream(values.split(","))
                    .map(next -> next.split("="))
                    .collect(Collectors.toMap(next -> next[0], next1 -> next1[1]));
        }
        return Map.of();
    }

    private String getDataFromMap(MaterialHolder material, Map<String, Object> data) {
        final var builder = new StringBuilder("minecraft:" + material.getPlatformName());
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
