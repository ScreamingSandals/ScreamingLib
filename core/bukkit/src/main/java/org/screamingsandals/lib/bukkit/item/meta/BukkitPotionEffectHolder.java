/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.item.meta;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitPotionEffectHolder extends BasicWrapper<PotionEffect> implements PotionEffectHolder {

    public BukkitPotionEffectHolder(PotionEffectType type) {
        this(new PotionEffect(type, 0, 0));
    }

    public BukkitPotionEffectHolder(PotionEffect wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
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
    public PotionEffectHolder withDuration(int duration) {
        try {
            return new BukkitPotionEffectHolder(
                    new PotionEffect(
                            wrappedObject.getType(),
                            duration,
                            wrappedObject.getAmplifier(),
                            wrappedObject.isAmbient(),
                            wrappedObject.hasParticles(),
                            wrappedObject.hasIcon()
                    )
            );
        } catch (Throwable ignored) {
            return new BukkitPotionEffectHolder(
                    new PotionEffect(
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
    public PotionEffectHolder withAmplifier(int amplifier) {
        try {
            return new BukkitPotionEffectHolder(
                    new PotionEffect(
                            wrappedObject.getType(),
                            wrappedObject.getDuration(),
                            amplifier,
                            wrappedObject.isAmbient(),
                            wrappedObject.hasParticles(),
                            wrappedObject.hasIcon()
                    )
            );
        } catch (Throwable ignored) {
            return new BukkitPotionEffectHolder(
                    new PotionEffect(
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
    public PotionEffectHolder withAmbient(boolean ambient) {
        try {
            return new BukkitPotionEffectHolder(
                    new PotionEffect(
                            wrappedObject.getType(),
                            wrappedObject.getDuration(),
                            wrappedObject.getAmplifier(),
                            ambient,
                            wrappedObject.hasParticles(),
                            wrappedObject.hasIcon()
                    )
            );
        } catch (Throwable ignored) {
            return new BukkitPotionEffectHolder(
                    new PotionEffect(
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
    public PotionEffectHolder withParticles(boolean particles) {
        try {
            return new BukkitPotionEffectHolder(
                    new PotionEffect(
                            wrappedObject.getType(),
                            wrappedObject.getDuration(),
                            wrappedObject.getAmplifier(),
                            wrappedObject.isAmbient(),
                            particles,
                            wrappedObject.hasIcon()
                    )
            );
        } catch (Throwable ignored) {
            return new BukkitPotionEffectHolder(
                    new PotionEffect(
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
    public PotionEffectHolder withIcon(boolean icon) {
        try {
            return new BukkitPotionEffectHolder(
                    new PotionEffect(
                            wrappedObject.getType(),
                            wrappedObject.getDuration(),
                            wrappedObject.getAmplifier(),
                            wrappedObject.isAmbient(),
                            wrappedObject.hasParticles(),
                            icon
                    )
            );
        } catch (Throwable ignored) {
            return this;
        }
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof PotionEffect || object instanceof PotionEffectHolder) {
            return equals(object);
        }
        return equals(PotionEffectHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public boolean isSameType(Object object) {
        if (object instanceof PotionEffect) {
            return ((PotionEffect) object).getType().equals(wrappedObject.getType());
        } else if (object instanceof BukkitPotionEffectHolder) {
            return ((BukkitPotionEffectHolder) object).wrappedObject.getType().equals(wrappedObject.getType());
        } else if (object instanceof PotionEffectType) {
            return object.equals(wrappedObject.getType());
        }
        return platformName().equals(PotionEffectHolder.ofOptional(object).map(PotionEffectHolder::platformName).orElse(null));
    }

    @Override
    public boolean isSameType(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::isSameType);
    }
}
