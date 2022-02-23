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

package org.screamingsandals.lib.minestom.item.meta;

import net.minestom.server.potion.CustomPotionEffect;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;
import java.util.Objects;

public class MinestomPotionEffectHolder extends BasicWrapper<CustomPotionEffect> implements PotionEffectHolder {
    protected MinestomPotionEffectHolder(CustomPotionEffect wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return Objects.requireNonNull(PotionEffect.fromId(wrappedObject.id())).name();
    }

    @Override
    public int duration() {
        return wrappedObject.duration();
    }

    @Override
    public int amplifier() {
        return wrappedObject.amplifier();
    }

    @Override
    public boolean ambient() {
        return wrappedObject.isAmbient();
    }

    @Override
    public boolean particles() {
        return wrappedObject.showParticles();
    }

    @Override
    public boolean icon() {
        return wrappedObject.showIcon();
    }

    @Override
    public PotionEffectHolder withDuration(int duration) {
        return new MinestomPotionEffectHolder(
                new CustomPotionEffect(
                        wrappedObject.id(),
                        wrappedObject.amplifier(),
                        duration,
                        wrappedObject.isAmbient(),
                        wrappedObject.showParticles(),
                        wrappedObject.showIcon()
                )
        );
    }

    @Override
    public PotionEffectHolder withAmplifier(int amplifier) {
        return new MinestomPotionEffectHolder(
                new CustomPotionEffect(
                        wrappedObject.id(),
                        (byte) amplifier,
                        wrappedObject.duration(),
                        wrappedObject.isAmbient(),
                        wrappedObject.showParticles(),
                        wrappedObject.showIcon()
                )
        );
    }

    @Override
    public PotionEffectHolder withAmbient(boolean ambient) {
        return new MinestomPotionEffectHolder(
                new CustomPotionEffect(
                        wrappedObject.id(),
                        wrappedObject.amplifier(),
                        wrappedObject.duration(),
                        ambient,
                        wrappedObject.showParticles(),
                        wrappedObject.showIcon()
                )
        );
    }

    @Override
    public PotionEffectHolder withParticles(boolean particles) {
        return new MinestomPotionEffectHolder(
                new CustomPotionEffect(
                        wrappedObject.id(),
                        wrappedObject.amplifier(),
                        wrappedObject.duration(),
                        wrappedObject.isAmbient(),
                        particles,
                        wrappedObject.showIcon()
                )
        );
    }

    @Override
    public PotionEffectHolder withIcon(boolean icon) {
        return new MinestomPotionEffectHolder(
                new CustomPotionEffect(
                        wrappedObject.id(),
                        wrappedObject.amplifier(),
                        wrappedObject.duration(),
                        wrappedObject.isAmbient(),
                        wrappedObject.showParticles(),
                        icon
                )
        );
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
            return ((PotionEffect) object).id() == wrappedObject.id();
        } else if (object instanceof MinestomPotionEffectHolder) {
            return ((MinestomPotionEffectHolder) object).wrappedObject.id() == wrappedObject.id();
        } else if (object instanceof Potion) {
            return ((Potion) object).effect().id() == wrappedObject.id();
        } else if (object instanceof CustomPotionEffect) {
            return ((CustomPotionEffect) object).id() == wrappedObject.id();
        }
        return platformName().equals(PotionEffectHolder.ofOptional(object).map(PotionEffectHolder::platformName).orElse(null));
    }

    @Override
    public boolean isSameType(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::isSameType);
    }
}
