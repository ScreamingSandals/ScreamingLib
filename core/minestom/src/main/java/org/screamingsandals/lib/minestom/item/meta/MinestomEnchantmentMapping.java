package org.screamingsandals.lib.minestom.item.meta;

import net.minestom.server.item.Enchantment;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.item.meta.EnchantmentMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class MinestomEnchantmentMapping extends EnchantmentMapping {
    public MinestomEnchantmentMapping() {
        enchantmentConverter
                .registerW2P(Enchantment.class, e -> Enchantment.fromNamespaceId(e.getPlatformName()))
                .registerP2W(Enchantment.class, e -> new EnchantmentHolder(e.name()));

        Enchantment.values().forEach(enchantment -> {
            final var holder = new EnchantmentHolder(enchantment.name());
            mapping.put(NamespacedMappingKey.of(enchantment.name()), holder);
            values.add(holder);
        });
    }
}
