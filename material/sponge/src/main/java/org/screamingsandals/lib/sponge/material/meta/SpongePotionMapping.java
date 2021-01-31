package org.screamingsandals.lib.sponge.material.meta;

import org.screamingsandals.lib.material.meta.PotionHolder;
import org.screamingsandals.lib.material.meta.PotionMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.potion.PotionType;
import org.spongepowered.api.registry.RegistryTypes;

@Service
public class SpongePotionMapping extends PotionMapping {
    public static void init() {
        PotionMapping.init(SpongePotionMapping::new);
    }

    public SpongePotionMapping() {
        potionConverter
                .registerW2P(PotionType.class, e -> Sponge.getGame().registries().registry(RegistryTypes.POTION_TYPE).findEntry(ResourceKey.resolve(e.getPlatformName())).orElseThrow().value())
                .registerP2W(PotionType.class, e -> new PotionHolder(Sponge.getGame().registries().registry(RegistryTypes.POTION_TYPE).findValueKey(e).orElseThrow().getFormatted()));

        Sponge.getGame().registries().registry(RegistryTypes.POTION_TYPE).forEach(type ->
                potionMapping.put(NamespacedMappingKey.of(type.key().getFormatted()), new PotionHolder(type.key().getFormatted()))
        );
    }
}
