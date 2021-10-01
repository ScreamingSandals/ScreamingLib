package org.screamingsandals.lib.bukkit.block;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.block.BlockTypeMapper;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BukkitBlockTypeMapper extends BlockTypeMapper {
    private final Map<String, Map<String, String>> defaultFlatteningBlockDataCache = new HashMap<>();

    public BukkitBlockTypeMapper() {
        blockTypeConverter
                .registerW2P(Material.class, holder -> Material.valueOf(holder.platformName()))
                .registerP2W(Material.class, material -> new BlockTypeHolder(material.name()));

        if (Version.isVersion(1, 13)) {
            blockTypeConverter
                    .registerP2W(BlockData.class, blockData -> new BlockTypeHolder(blockData.getMaterial().name(), getDataFromString(blockData.getAsString())))
                    .registerW2P(BlockData.class, holder -> Bukkit.createBlockData(getDataFromMap(holder)));
        } else {
            blockTypeConverter
                    .registerP2W(MaterialData.class, data -> BlockTypeHolder.of(data.getItemType() + ":" + data.getData()))
                    .registerW2P(MaterialData.class, holder -> holder.as(Material.class).getNewData(holder.legacyData()));
        }

        Arrays.stream(Material.values())
                .filter(t -> !t.name().startsWith("LEGACY") && t.isBlock())
                .forEach(material -> {
                    var holder = new BlockTypeHolder(material.name());
                    mapping.put(NamespacedMappingKey.of(material.name()), holder);
                    values.add(holder);
                });
    }

    protected Map<String, String> getDataFromString(String data) {
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

    @Override
    protected BlockTypeHolder normalize(BlockTypeHolder abnormal) {
        try {
            if (Version.isVersion(1, 13)) {
                var cache = defaultFlatteningBlockDataCache.get(abnormal.platformName());
                if (cache == null) {
                    cache = getDataFromString(abnormal.as(Material.class).createBlockData().getAsString());
                    defaultFlatteningBlockDataCache.put(abnormal.platformName(), cache);
                }
                if (cache.isEmpty()) {
                    return abnormal;
                }
                var flatteningData = abnormal.flatteningData();
                if (flatteningData != null && !flatteningData.isEmpty()) {
                    var clone = new HashMap<>(flatteningData);
                    cache.forEach((s, s2) -> {
                        if (!clone.containsKey(s)) {
                            clone.put(s, s2);
                        }
                    });
                }
                return abnormal.withFlatteningData(cache);
            } else {
                // non-flattening versions don't have flattening data
                return abnormal.withFlatteningData(null);
            }
        } catch (Exception ignored) {}
        return abnormal;
    }

    @Override
    public String getStateDataFromMap(Map<String, String> data) {
        final var builder = new StringBuilder();
        if (data != null && !data.isEmpty()) {
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

    @Override
    protected boolean isLegacy() {
        return !Version.isVersion(1, 13);
    }

    @Override
    protected boolean isSolid0(BlockTypeHolder blockType) {
        return blockType.as(Material.class).isSolid();
    }

    @Override
    protected boolean isTransparent0(BlockTypeHolder blockType) {
        return blockType.as(Material.class).isTransparent();
    }

    @Override
    protected boolean isFlammable0(BlockTypeHolder blockType) {
        return blockType.as(Material.class).isFlammable();
    }

    @Override
    protected boolean isBurnable0(BlockTypeHolder blockType) {
        return blockType.as(Material.class).isBurnable();
    }

    @Override
    protected boolean isOccluding0(BlockTypeHolder blockType) {
        return blockType.as(Material.class).isOccluding();
    }

    @Override
    protected boolean hasGravity0(BlockTypeHolder blockType) {
        return blockType.as(Material.class).hasGravity();
    }

    protected String getDataFromMap(BlockTypeHolder material) {
        final var builder = new StringBuilder("minecraft:" + material.platformName().toLowerCase());
        final var data = material.flatteningData();
        if (data != null && !data.isEmpty()) {
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
