package org.screamingsandals.lib.bukkit.material.meta;

import org.bukkit.enchantments.Enchantment;
import org.screamingsandals.lib.material.meta.EnchantmentHolder;
import org.screamingsandals.lib.material.meta.EnchantmentMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitEnchantmentMapping extends EnchantmentMapping {

    public static void init() {
        EnchantmentMapping.init(BukkitEnchantmentMapping::new);
    }

    public BukkitEnchantmentMapping() {
        enchantmentConverter
                .registerW2P(Enchantment.class, e -> Enchantment.getByName(e.getPlatformName()))
                .registerP2W(Enchantment.class, e -> new EnchantmentHolder(e.getName()));

        Arrays.stream(Enchantment.values()).forEach(enchantment -> enchantmentMapping.put(NamespacedMappingKey.of(enchantment.getName()), new EnchantmentHolder(enchantment.getName())));
    }

}
