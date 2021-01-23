package org.screamingsandals.lib.sponge.material.meta;

import org.screamingsandals.lib.material.meta.PotionHolder;
import org.screamingsandals.lib.material.meta.PotionMapping;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.PlatformMapping;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.potion.PotionType;
import org.spongepowered.api.registry.RegistryTypes;

@PlatformMapping(platform = PlatformType.SPONGE)
public class SpongePotionMapping extends PotionMapping {
    public static void init() {
        PotionMapping.init(SpongePotionMapping::new);
    }

    public SpongePotionMapping() {
        potionConverter
                .registerW2P(PotionType.class, e -> Sponge.getGame().registries().registry(RegistryTypes.POTION_TYPE).findEntry(ResourceKey.resolve(e.getPlatformName())).orElseThrow().value())
                .registerP2W(PotionType.class, e -> new PotionHolder(Sponge.getGame().registries().registry(RegistryTypes.POTION_TYPE).findValueKey(e).orElseThrow().getFormatted()));

        Sponge.getGame().registries().registry(RegistryTypes.POTION_TYPE).forEach(itemType ->
                potionMapping.put(itemType.key().getNamespace().equals(ResourceKey.MINECRAFT_NAMESPACE) ? itemType.key().getValue().toUpperCase() : itemType.key().getFormatted().toUpperCase(), new PotionHolder(itemType.key().getFormatted()))
        );
    }
}
