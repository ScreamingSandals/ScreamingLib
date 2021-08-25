package org.screamingsandals.lib.bukkit.bukkit;

import org.bukkit.Material;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.block.BlockTypeMapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitBlockTypeMapper extends BlockTypeMapper {
    public BukkitBlockTypeMapper() {
        blockTypeConverter
                .registerW2P(Material.class, holder -> Material.valueOf(holder.platformName()))
                .registerP2W(Material.class, material -> new BlockTypeHolder(material.name()));

        Arrays.stream(Material.values())
                .filter(t -> !t.name().startsWith("LEGACY") && t.isBlock())
                .forEach(material ->
                        mapping.put(NamespacedMappingKey.of(material.name()), new BlockTypeHolder(material.name()))
                );
    }
}
