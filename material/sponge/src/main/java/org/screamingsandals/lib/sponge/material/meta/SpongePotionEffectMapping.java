package org.screamingsandals.lib.sponge.material.meta;

import org.screamingsandals.lib.material.meta.PotionEffectHolder;
import org.screamingsandals.lib.material.meta.PotionEffectMapping;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.PlatformMapping;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.registry.RegistryTypes;

@PlatformMapping(platform = PlatformType.SPONGE)
public class SpongePotionEffectMapping extends PotionEffectMapping {
    public static void init() {
        PotionEffectMapping.init(SpongePotionEffectMapping::new);
    }

    public SpongePotionEffectMapping() {
        potionEffectConverter
                .registerW2P(PotionEffect.class, e -> PotionEffect.builder()
                        .potionType(Sponge.getGame().registries().registry(RegistryTypes.POTION_EFFECT_TYPE).findEntry(ResourceKey.resolve(e.getPlatformName())).orElseThrow().value())
                        .duration(e.getDuration())
                        .amplifier(e.getAmplifier())
                        .ambient(e.isAmbient())
                        .showParticles(e.isParticles())
                        .showIcon(e.isIcon())
                        .build())
                .registerW2P(PotionEffectType.class, e -> Sponge.getGame().registries().registry(RegistryTypes.POTION_EFFECT_TYPE).findEntry(ResourceKey.resolve(e.getPlatformName())).orElseThrow().value())
                .registerP2W(PotionEffect.class, e -> new PotionEffectHolder(
                        Sponge.getGame().registries().registry(RegistryTypes.POTION_EFFECT_TYPE).findValueKey(e.getType()).orElseThrow().value(),
                        e.getDuration(),
                        e.getAmplifier(),
                        e.isAmbient(),
                        e.showsParticles(),
                        e.showsIcon()
                ))
                .registerP2W(PotionEffectType.class, e -> new PotionEffectHolder(Sponge.getGame().registries().registry(RegistryTypes.POTION_EFFECT_TYPE).findValueKey(e).orElseThrow().value()));

        Sponge.getGame().registries().registry(RegistryTypes.POTION_EFFECT_TYPE).forEach(itemType ->
                potionEffectMapping.put(itemType.key().getNamespace().equals(ResourceKey.MINECRAFT_NAMESPACE) ? itemType.key().getValue().toUpperCase() : itemType.key().getFormatted().toUpperCase(), new PotionEffectHolder(itemType.key().getFormatted()))
        );
    }
}
