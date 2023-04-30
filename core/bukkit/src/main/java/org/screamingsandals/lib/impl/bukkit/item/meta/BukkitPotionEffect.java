/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.impl.bukkit.item.meta;

import lombok.experimental.ExtensionMethod;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.item.meta.PotionEffect;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;
import java.util.Locale;

@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitPotionEffect extends BasicWrapper<org.bukkit.potion.PotionEffect> implements PotionEffect {

    public BukkitPotionEffect(@NotNull PotionEffectType type) {
        this(new org.bukkit.potion.PotionEffect(type, 0, 0));
    }

    public BukkitPotionEffect(@NotNull org.bukkit.potion.PotionEffect wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.getType().getName();
    }

    @Override
    public int duration() {
        return wrappedObject.getDuration();
    }

    @Override
    public int amplifier() {
        return wrappedObject.getAmplifier();
    }

    @Override
    public boolean ambient() {
        return wrappedObject.isAmbient();
    }

    @Override
    public boolean particles() {
        return wrappedObject.hasParticles();
    }

    @Override
    public boolean icon() {
        try {
            return wrappedObject.hasIcon();
        } catch (Throwable ignored) {
            return false;
        }
    }

    @Override
    public @NotNull PotionEffect withDuration(int duration) {
        if (BukkitFeature.POTION_EFFECT_CONSTRUCTOR_WITH_ICON.isSupported()) {
            return new BukkitPotionEffect(
                    new org.bukkit.potion.PotionEffect(
                            wrappedObject.getType(),
                            duration,
                            wrappedObject.getAmplifier(),
                            wrappedObject.isAmbient(),
                            wrappedObject.hasParticles(),
                            wrappedObject.hasIcon()
                    )
            );
        } else {
            return new BukkitPotionEffect(
                    new org.bukkit.potion.PotionEffect(
                            wrappedObject.getType(),
                            duration,
                            wrappedObject.getAmplifier(),
                            wrappedObject.isAmbient(),
                            wrappedObject.hasParticles()
                    )
            );
        }
    }

    @Override
    public @NotNull PotionEffect withAmplifier(int amplifier) {
        if (BukkitFeature.POTION_EFFECT_CONSTRUCTOR_WITH_ICON.isSupported()) {
            return new BukkitPotionEffect(
                    new org.bukkit.potion.PotionEffect(
                            wrappedObject.getType(),
                            wrappedObject.getDuration(),
                            amplifier,
                            wrappedObject.isAmbient(),
                            wrappedObject.hasParticles(),
                            wrappedObject.hasIcon()
                    )
            );
        } else {
            return new BukkitPotionEffect(
                    new org.bukkit.potion.PotionEffect(
                            wrappedObject.getType(),
                            wrappedObject.getDuration(),
                            amplifier,
                            wrappedObject.isAmbient(),
                            wrappedObject.hasParticles()
                    )
            );
        }
    }

    @Override
    public @NotNull PotionEffect withAmbient(boolean ambient) {
        if (BukkitFeature.POTION_EFFECT_CONSTRUCTOR_WITH_ICON.isSupported()) {
            return new BukkitPotionEffect(
                    new org.bukkit.potion.PotionEffect(
                            wrappedObject.getType(),
                            wrappedObject.getDuration(),
                            wrappedObject.getAmplifier(),
                            ambient,
                            wrappedObject.hasParticles(),
                            wrappedObject.hasIcon()
                    )
            );
        } else {
            return new BukkitPotionEffect(
                    new org.bukkit.potion.PotionEffect(
                            wrappedObject.getType(),
                            wrappedObject.getDuration(),
                            wrappedObject.getAmplifier(),
                            ambient,
                            wrappedObject.hasParticles()
                    )
            );
        }
    }

    @Override
    public @NotNull PotionEffect withParticles(boolean particles) {
        if (BukkitFeature.POTION_EFFECT_CONSTRUCTOR_WITH_ICON.isSupported()) {
            return new BukkitPotionEffect(
                    new org.bukkit.potion.PotionEffect(
                            wrappedObject.getType(),
                            wrappedObject.getDuration(),
                            wrappedObject.getAmplifier(),
                            wrappedObject.isAmbient(),
                            particles,
                            wrappedObject.hasIcon()
                    )
            );
        } else {
            return new BukkitPotionEffect(
                    new org.bukkit.potion.PotionEffect(
                            wrappedObject.getType(),
                            wrappedObject.getDuration(),
                            wrappedObject.getAmplifier(),
                            wrappedObject.isAmbient(),
                            particles
                    )
            );
        }
    }

    @Override
    public @NotNull PotionEffect withIcon(boolean icon) {
        if (BukkitFeature.POTION_EFFECT_CONSTRUCTOR_WITH_ICON.isSupported()) {
            return new BukkitPotionEffect(
                    new org.bukkit.potion.PotionEffect(
                            wrappedObject.getType(),
                            wrappedObject.getDuration(),
                            wrappedObject.getAmplifier(),
                            wrappedObject.isAmbient(),
                            wrappedObject.hasParticles(),
                            icon
                    )
            );
        } else {
            return this;
        }
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof org.bukkit.potion.PotionEffect || object instanceof PotionEffect) {
            return equals(object);
        }
        return equals(PotionEffect.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public boolean isSameType(@Nullable Object object) {
        if (object instanceof org.bukkit.potion.PotionEffect) {
            return ((org.bukkit.potion.PotionEffect) object).getType().equals(wrappedObject.getType());
        } else if (object instanceof BukkitPotionEffect) {
            return ((BukkitPotionEffect) object).wrappedObject.getType().equals(wrappedObject.getType());
        } else if (object instanceof PotionEffectType) {
            return object.equals(wrappedObject.getType());
        }
        return platformName().equals(PotionEffect.ofNullable(object).mapOrNull(PotionEffect::platformName));
    }

    @Override
    public boolean isSameType(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::isSameType);
    }

    @Override
    public @NotNull ResourceLocation location() {
        if (BukkitFeature.POTION_EFFECT_KEYED.isSupported()) {
            var namespacedKey = wrappedObject.getType().getKey();
            return ResourceLocation.of(namespacedKey.getNamespace(), namespacedKey.getKey());
        } else {
            return ResourceLocation.of(getLocationPath(wrappedObject.getType()));
        }
    }

    public static @NotNull String getLocationPath(@NotNull PotionEffectType effectType) {
        String path = effectType.getName();
        switch (path) {
            //<editor-fold desc="Enum constants -> 1.18 flattening names" defaultstate="collapsed">
            // @formatter:off

            //case "SPEED": path = "speed"; break; - same
            case "SLOW": path = "slowness"; break;
            case "FAST_DIGGING": path = "haste"; break;
            case "SLOW_DIGGING": path = "mining_fatigue"; break;
            case "INCREASE_DAMAGE": path = "strength"; break;
            case "HEAL": path = "instant_health"; break;
            case "HARM": path = "instant_damage"; break;
            case "JUMP": path = "jump_boost"; break;
            case "CONFUSION": path = "nausea"; break;
            //case "REGENERATION": path = "regeneration"; break; - same
            case "DAMAGE_RESISTANCE": path = "resistance"; break;
            //case "FIRE_RESISTANCE": path = "fire_resistance"; break; - same
            //case "WATER_BREATHING": path = "water_breathing"; break; - same
            //case "INVISIBILITY": path = "invisibility"; break; - same
            //case "BLINDNESS": path = "blindness"; break; - same
            //case "NIGHT_VISION": path = "night_vision"; break; - same
            //case "HUNGER": path = "hunger"; break; - same
            //case "WEAKNESS": path = "weakness"; break; - same
            //case "POISON": path = "poison"; break; - same
            //case "WITHER": path = "wither"; break; - same
            //case "HEALTH_BOOST": path = "health_boost"; break; - same
            //case "ABSORPTION": path = "absorption"; break; - same
            //case "SATURATION": path = "saturation"; break; - same
            //case "GLOWING": path = "glowing"; break; - same
            //case "LEVITATION": path = "levitation"; break; - same
            //case "LUCK": path = "luck"; break; - same
            //case "UNLUCK": path = "unluck"; break; - same
            //case "SLOW_FALLING": path = "slow_falling"; break; - same
            //case "CONDUIT_POWER": path = "conduit_power"; break; - same
            //case "DOLPHINS_GRACE": path = "dolphins_grace"; break; - same
            //case "BAD_OMEN": path = "bad_omen"; break; - same
            //case "HERO_OF_THE_VILLAGE": path = "hero_of_the_village"; break; - same

            // @formatter:on
            //</editor-fold>
        }
        return path.toLowerCase(Locale.ROOT);
    }
}
