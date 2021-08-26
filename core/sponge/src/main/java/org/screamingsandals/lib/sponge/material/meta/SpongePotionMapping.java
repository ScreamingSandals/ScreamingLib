package org.screamingsandals.lib.sponge.material.meta;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.item.meta.PotionHolder;
import org.screamingsandals.lib.item.meta.PotionMapping;
import org.screamingsandals.lib.sponge.utils.SpongeRegistryMapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.spongepowered.api.item.potion.PotionType;
import org.spongepowered.api.registry.RegistryType;
import org.spongepowered.api.registry.RegistryTypes;

@Service
public class SpongePotionMapping extends PotionMapping implements SpongeRegistryMapper<PotionType> {
    public static void init() {
        PotionMapping.init(SpongePotionMapping::new);
    }

    public SpongePotionMapping() {
        potionConverter
                .registerW2P(PotionType.class, e -> getEntry(e.getPlatformName()).value())
                .registerP2W(PotionType.class, e -> new PotionHolder(getKeyByValue(e).getFormatted()));

        getAllKeys().forEach(key ->
                mapping.put(NamespacedMappingKey.of(key.getFormatted()), new PotionHolder(key.getFormatted()))
        );
    }

    @Override
    @NotNull
    public RegistryType<PotionType> getRegistryType() {
        return RegistryTypes.POTION_TYPE;
    }
}
