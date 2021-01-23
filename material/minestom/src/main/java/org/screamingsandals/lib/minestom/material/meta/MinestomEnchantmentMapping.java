package org.screamingsandals.lib.minestom.material.meta;

import net.minestom.server.item.Enchantment;
import org.screamingsandals.lib.material.meta.EnchantmentHolder;
import org.screamingsandals.lib.material.meta.EnchantmentMapping;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.PlatformMapping;

import java.util.Arrays;

@PlatformMapping(platform = PlatformType.MINESTOM)
public class MinestomEnchantmentMapping extends EnchantmentMapping {

    public static void init() {
        EnchantmentMapping.init(MinestomEnchantmentMapping::new);
    }

    public MinestomEnchantmentMapping() {
        enchantmentConverter
                .registerW2P(Enchantment.class, e -> Enchantment.valueOf(e.getPlatformName()))
                .registerP2W(Enchantment.class, e -> new EnchantmentHolder(e.name()));

        Arrays.stream(Enchantment.values()).forEach(enchantment -> enchantmentMapping.put(enchantment.name().toUpperCase(), new EnchantmentHolder(enchantment.name())));
    }
}
