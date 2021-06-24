package org.screamingsandals.lib.sponge.material.meta;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.material.meta.PotionEffectHolder;
import org.screamingsandals.lib.material.meta.PotionEffectMapping;
import org.screamingsandals.lib.sponge.utils.SpongeRegistryMapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.registry.RegistryType;
import org.spongepowered.api.registry.RegistryTypes;

@Service
public class SpongePotionEffectMapping extends PotionEffectMapping implements SpongeRegistryMapper<PotionEffectType> {
    public static void init() {
        PotionEffectMapping.init(SpongePotionEffectMapping::new);
    }

    public SpongePotionEffectMapping() {
        potionEffectConverter
                .registerW2P(PotionEffect.class, e -> PotionEffect.builder()
                        .potionType(getEntry(e.getPlatformName()).value())
                        .duration(e.getDuration())
                        .amplifier(e.getAmplifier())
                        .ambient(e.isAmbient())
                        .showParticles(e.isParticles())
                        .showIcon(e.isIcon())
                        .build())
                .registerW2P(PotionEffectType.class, e -> getEntry(e.getPlatformName()).value())
                .registerP2W(PotionEffect.class, e -> new PotionEffectHolder(
                        getKeyByValue(e.getType()).getFormatted(),
                        e.getDuration(),
                        e.getAmplifier(),
                        e.isAmbient(),
                        e.showsParticles(),
                        e.showsIcon()
                ))
                .registerP2W(PotionEffectType.class, e -> new PotionEffectHolder(getKeyByValue(e).getFormatted()));

        getAllKeys().forEach(key ->
                mapping.put(NamespacedMappingKey.of(key.getFormatted()), new PotionEffectHolder(key.getFormatted()))
        );
    }

    @Override
    @NotNull
    public RegistryType<PotionEffectType> getRegistryType() {
        return RegistryTypes.POTION_EFFECT_TYPE;
    }
}
