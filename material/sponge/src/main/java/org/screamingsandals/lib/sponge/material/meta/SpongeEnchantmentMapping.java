package org.screamingsandals.lib.sponge.material.meta;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.material.meta.EnchantmentHolder;
import org.screamingsandals.lib.material.meta.EnchantmentMapping;
import org.screamingsandals.lib.sponge.utils.SpongeRegistryMapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.registry.RegistryType;
import org.spongepowered.api.registry.RegistryTypes;

@Service
public class SpongeEnchantmentMapping extends EnchantmentMapping implements SpongeRegistryMapper<EnchantmentType> {
    public static void init() {
        EnchantmentMapping.init(SpongeEnchantmentMapping::new);
    }

    public SpongeEnchantmentMapping() {
        enchantmentConverter
                .registerW2P(Enchantment.class, e -> Enchantment.of(getEntry(e.getPlatformName()).value(), e.getLevel()))
                .registerW2P(EnchantmentType.class, e -> getEntry(e.getPlatformName()).value())
                .registerP2W(Enchantment.class, e -> new EnchantmentHolder(getKeyByValue(e.getType()).getFormatted(), e.getLevel()))
                .registerP2W(EnchantmentType.class, e -> new EnchantmentHolder(getKeyByValue(e).getFormatted()));

        getAllKeys().forEach(key ->
                mapping.put(NamespacedMappingKey.of(key.getFormatted()), new EnchantmentHolder(key.getFormatted()))
        );
    }

    @Override
    @NotNull
    public RegistryType<EnchantmentType> getRegistryType() {
        return RegistryTypes.ENCHANTMENT_TYPE;
    }
}
