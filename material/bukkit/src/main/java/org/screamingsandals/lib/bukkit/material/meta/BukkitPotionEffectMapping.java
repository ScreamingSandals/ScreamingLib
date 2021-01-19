package org.screamingsandals.lib.bukkit.material.meta;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.screamingsandals.lib.material.meta.PotionEffectHolder;
import org.screamingsandals.lib.material.meta.PotionEffectMapping;

import java.util.Arrays;

public class BukkitPotionEffectMapping extends PotionEffectMapping {
    public static void init() {
        PotionEffectMapping.init(BukkitPotionEffectMapping::new);
    }

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

        Arrays.stream(PotionEffectType.values()).forEach(potionEffectType -> potionEffectMapping.put(potionEffectType.getName().toUpperCase(), new PotionEffectHolder(potionEffectType.getName())));
    }
}
