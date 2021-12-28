package org.screamingsandals.lib.bukkit.item.meta;

import org.bukkit.enchantments.Enchantment;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.item.meta.EnchantmentMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitEnchantmentMapping extends EnchantmentMapping {

    public BukkitEnchantmentMapping() {
        enchantmentConverter
                .registerP2W(Enchantment.class, BukkitEnchantmentHolder::new);

        Arrays.stream(Enchantment.values()).forEach(enchantment -> {
            var holder = new BukkitEnchantmentHolder(enchantment);
            mapping.put(NamespacedMappingKey.of(enchantment.getName()), holder);
            values.add(holder);
        });
    }

}
