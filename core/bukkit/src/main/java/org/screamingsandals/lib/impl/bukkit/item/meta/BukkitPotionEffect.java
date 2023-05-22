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

import java.util.Arrays;

@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitPotionEffect extends BasicWrapper<org.bukkit.potion.PotionEffect> implements PotionEffect {

    public BukkitPotionEffect(@NotNull PotionEffectType type) {
        this(new org.bukkit.potion.PotionEffect(type, 0, 0));
    }

    public BukkitPotionEffect(@NotNull org.bukkit.potion.PotionEffect wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public org.screamingsandals.lib.item.meta.@NotNull PotionEffectType type() {
        return org.screamingsandals.lib.item.meta.PotionEffectType.of(wrappedObject.getType());
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
}
