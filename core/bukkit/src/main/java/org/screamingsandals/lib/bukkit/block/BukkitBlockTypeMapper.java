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
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BukkitBlockTypeMapper extends BlockTypeMapper {
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
                    .registerP2W(MaterialData.class, data -> BlockTypeHolder.of(data.getItemType() + ":" + data.getData()).withFlatteningData(LegacyBlockDataConverter.convertMaterialData(data)))
                    .registerW2P(MaterialData.class, holder -> LegacyBlockDataConverter.asMaterialData(holder.as(Material.class), holder.legacyData(), holder.flatteningData()));
        }

        Arrays.stream(Material.values())
                .filter(t -> !t.name().startsWith("LEGACY") && t.isBlock())
                .forEach(material ->
                        mapping.put(NamespacedMappingKey.of(material.name()), new BlockTypeHolder(material.name()))
                );
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
