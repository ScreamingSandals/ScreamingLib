package org.screamingsandals.lib.bukkit.item.meta;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.item.meta.PotionEffectMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.key.NumericMappingKey;

import java.util.Arrays;

@Service
public class BukkitPotionEffectMapping extends PotionEffectMapping {
    @SuppressWarnings("ConstantConditions")
    public BukkitPotionEffectMapping() {
        potionEffectConverter
                .registerW2P(PotionEffectType.class, potionEffectHolder -> PotionEffectType.getByName(potionEffectHolder.getPlatformName()))
                .registerP2W(PotionEffectType.class, potionEffectType -> new PotionEffectHolder(potionEffectType.getName()))
                .registerW2P(PotionEffect.class, potionEffectHolder -> {
                    try {
                        return new PotionEffect(
                                PotionEffectType.getByName(potionEffectHolder.getPlatformName()),
                                potionEffectHolder.getDuration(),
                                potionEffectHolder.getAmplifier(),
                                potionEffectHolder.isAmbient(),
                                potionEffectHolder.isParticles(),
                                potionEffectHolder.isIcon());
                    } catch (Throwable ignored) {
                        return new PotionEffect(
                                PotionEffectType.getByName(potionEffectHolder.getPlatformName()),
                                potionEffectHolder.getDuration(),
                                potionEffectHolder.getAmplifier(),
                                potionEffectHolder.isAmbient(),
                                potionEffectHolder.isParticles());
                    }
                })
                .registerP2W(PotionEffect.class, potionEffect -> {
                        var holder = new PotionEffectHolder(potionEffect.getType().getName())
                                .duration(potionEffect.getDuration())
                                .amplifier(potionEffect.getAmplifier())
                                .ambient(potionEffect.isAmbient())
                                .particles(potionEffect.hasParticles());
                        try {
                            holder = holder.icon(potionEffect.hasIcon());
                        } catch (Throwable ignored) {}

                        return holder;
                });

        Arrays.stream(PotionEffectType.values()).forEach(potionEffectType -> {
            if (potionEffectType != null) { // Yeah, this is possible
                var holder = new PotionEffectHolder(potionEffectType.getName());
                mapping.put(NamespacedMappingKey.of(potionEffectType.getName()), holder);
                mapping.put(NumericMappingKey.of(potionEffectType.getId()), holder); // compatibility with older bw shops
                values.add(holder);
            }
        });
    }
}
