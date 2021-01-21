package org.screamingsandals.lib.sponge.material.meta;

import org.screamingsandals.lib.material.meta.EnchantmentHolder;
import org.screamingsandals.lib.material.meta.EnchantmentMapping;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.AutoInitialization;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.registry.RegistryTypes;

@AutoInitialization(platform = PlatformType.SPONGE)
public class SpongeEnchantmentMapping extends EnchantmentMapping {
    public static void init() {
        EnchantmentMapping.init(SpongeEnchantmentMapping::new);
    }

    public SpongeEnchantmentMapping() {
        enchantmentConverter
                .registerW2P(Enchantment.class, e -> Enchantment.of(Sponge.getGame().registries().registry(RegistryTypes.ENCHANTMENT_TYPE).findEntry(ResourceKey.resolve(e.getPlatformName())).orElseThrow().value(), e.getLevel()))
                .registerW2P(EnchantmentType.class, e -> Sponge.getGame().registries().registry(RegistryTypes.ENCHANTMENT_TYPE).findEntry(ResourceKey.resolve(e.getPlatformName())).orElseThrow().value())
                .registerP2W(Enchantment.class, e -> new EnchantmentHolder(Sponge.getGame().registries().registry(RegistryTypes.ENCHANTMENT_TYPE).findValueKey(e.getType()).orElseThrow().value(), e.getLevel()))
                .registerP2W(EnchantmentType.class, e -> new EnchantmentHolder(Sponge.getGame().registries().registry(RegistryTypes.ENCHANTMENT_TYPE).findValueKey(e).orElseThrow().value()));

        Sponge.getGame().registries().registry(RegistryTypes.ENCHANTMENT_TYPE).forEach(itemType ->
                enchantmentMapping.put(itemType.key().getNamespace().equals(ResourceKey.MINECRAFT_NAMESPACE) ? itemType.key().getValue().toUpperCase() : itemType.key().getFormatted().toUpperCase(), new EnchantmentHolder(itemType.key().getFormatted()))
        );
    }
}
