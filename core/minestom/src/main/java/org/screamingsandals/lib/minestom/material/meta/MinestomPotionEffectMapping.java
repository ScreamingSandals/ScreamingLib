package org.screamingsandals.lib.minestom.material.meta;

import net.minestom.server.potion.CustomPotionEffect;
import net.minestom.server.potion.PotionEffect;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.item.meta.PotionEffectMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class MinestomPotionEffectMapping extends PotionEffectMapping {
    public static void init() {
        PotionEffectMapping.init(MinestomPotionEffectMapping::new);
    }

    public MinestomPotionEffectMapping() {
        potionEffectConverter
                .registerP2W(PotionEffect.class, potionEffect -> new PotionEffectHolder(potionEffect.name()))
                .registerW2P(PotionEffect.class, potionEffectHolder -> PotionEffect.valueOf(potionEffectHolder.getPlatformName()))
                .registerP2W(CustomPotionEffect.class, customPotionEffect -> new PotionEffectHolder(
                        PotionEffect.fromId(customPotionEffect.getId()).name(),
                        customPotionEffect.getAmplifier(),
                        customPotionEffect.getDuration(),
                        customPotionEffect.isAmbient(),
                        customPotionEffect.showParticles(),
                        customPotionEffect.showIcon()
                ))
                .registerW2P(CustomPotionEffect.class, potionEffectHolder -> new CustomPotionEffect(
                        (byte) PotionEffect.valueOf(potionEffectHolder.getPlatformName()).getId(),
                        (byte) potionEffectHolder.getAmplifier(),
                        potionEffectHolder.getDuration(),
                        potionEffectHolder.isAmbient(),
                        potionEffectHolder.isParticles(),
                        potionEffectHolder.isIcon()
                ));


        Arrays.stream(PotionEffect.values()).forEach(potionEffect -> mapping.put(NamespacedMappingKey.of(potionEffect.getNamespaceID()), new PotionEffectHolder(potionEffect.name())));
    }
}
