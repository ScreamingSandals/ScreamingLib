package org.screamingsandals.lib.bukkit.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.bukkit.BukkitItemBlockIdsRemapper;
import org.screamingsandals.lib.item.ItemTypeMapper;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitItemTypeMapper extends ItemTypeMapper {
    public BukkitItemTypeMapper() {
        if (BukkitItemBlockIdsRemapper.getBPlatform() == Platform.JAVA_LEGACY) {
            itemTypeConverter
                    .registerP2W(Material.class, BukkitItemTypeLegacyHolder::new)
                    .registerP2W(ItemStack.class, stack -> new BukkitItemTypeLegacyHolder(stack.getType(), stack.getDurability()));

            Arrays.stream(Material.values())
                    .forEach(material -> {
                        var holder = new BukkitItemTypeLegacyHolder(material);
                        mapping.put(NamespacedMappingKey.of(material.name()), holder);
                        values.add(holder);
                    });
        } else {
            itemTypeConverter
                    .registerP2W(Material.class, BukkitItemTypeHolder::new)
                    .registerP2W(ItemStack.class, stack -> new BukkitItemTypeHolder(stack.getType()));


            Arrays.stream(Material.values())
                    .filter(t -> !t.name().startsWith("LEGACY"))
                    .filter(Material::isItem)
                    .forEach(material -> {
                        var holder = new BukkitItemTypeHolder(material);
                        mapping.put(NamespacedMappingKey.of(material.name()), holder);
                        values.add(holder);
                    });
        }
    }
}
