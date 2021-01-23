package org.screamingsandals.lib.bukkit.material.meta;

import org.bukkit.enchantments.Enchantment;
import org.screamingsandals.lib.material.meta.EnchantmentHolder;
import org.screamingsandals.lib.material.meta.EnchantmentMapping;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.PlatformMapping;

import java.util.Arrays;

@PlatformMapping(platform = PlatformType.BUKKIT)
public class BukkitEnchantmentMapping extends EnchantmentMapping {

    public static void init() {
        EnchantmentMapping.init(BukkitEnchantmentMapping::new);
    }

    public BukkitEnchantmentMapping() {
        enchantmentConverter
                .registerW2P(Enchantment.class, e -> Enchantment.getByName(e.getPlatformName()))
                .registerP2W(Enchantment.class, e -> new EnchantmentHolder(e.getName()));

        Arrays.stream(Enchantment.values()).forEach(enchantment -> enchantmentMapping.put(enchantment.getName().toUpperCase(), new EnchantmentHolder(enchantment.getName())));
    }

}
