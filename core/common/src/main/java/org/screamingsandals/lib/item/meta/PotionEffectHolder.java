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

package org.screamingsandals.lib.item.meta;

import com.iamceph.resulter.core.pack.ProtoWrapper;
import org.jetbrains.annotations.Contract;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.ProtoPotionEffect;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface PotionEffectHolder extends ComparableWrapper, ProtoWrapper<ProtoPotionEffect> {
    String platformName();

    int duration();

    int amplifier();

    boolean ambient();

    boolean particles();

    boolean icon();

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default String getPlatformName() {
        return platformName();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default int getDuration() {
        return duration();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default int getAmplifier() {
        return amplifier();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default boolean isAmbient() {
        return ambient();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default boolean isParticles() {
        return particles();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default boolean isIcon() {
        return icon();
    }

    @Contract(value = "_ -> new", pure = true)
    PotionEffectHolder withDuration(int duration);

    /**
     * Inconsistent naming: Holders use "with" prefix
     */
    @Deprecated(forRemoval = true)
    @Contract(value = "_ -> new", pure = true)
    default PotionEffectHolder duration(int duration) {
        return withDuration(duration);
    }

    @Contract(value = "_ -> new", pure = true)
    PotionEffectHolder withAmplifier(int amplifier);

    /**
     * Inconsistent naming: Holders use "with" prefix
     */
    @Deprecated(forRemoval = true)
    @Contract(value = "_ -> new", pure = true)
    default PotionEffectHolder amplifier(int amplifier) {
        return withAmplifier(amplifier);
    }

    @Contract(value = "_ -> new", pure = true)
    PotionEffectHolder withAmbient(boolean ambient);

    /**
     * Inconsistent naming: Holders use "with" prefix
     */
    @Deprecated(forRemoval = true)
    @Contract(value = "_ -> new", pure = true)
    default PotionEffectHolder ambient(boolean ambient) {
        return withAmbient(ambient);
    }

    @Contract(value = "_ -> new", pure = true)
    PotionEffectHolder withParticles(boolean particles);

    /**
     * Inconsistent naming: Holders use "with" prefix
     */
    @Deprecated(forRemoval = true)
    @Contract(value = "_ -> new", pure = true)
    default PotionEffectHolder particles(boolean particles) {
        return withParticles(particles);
    }

    @Contract(value = "_ -> new", pure = true)
    PotionEffectHolder withIcon(boolean icon);

    /**
     * Inconsistent naming: Holders use "with" prefix
     */
    @Deprecated(forRemoval = true)
    @Contract(value = "_ -> new", pure = true)
    default PotionEffectHolder icon(boolean icon) {
        return withIcon(icon);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    @Override
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    boolean isSameType(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    boolean isSameType(Object... objects);

    /**
     * Inconsistent naming (should be isSameType like in other holders)
     */
    @Deprecated(forRemoval = true)
    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    default boolean isType(Object object) {
        return isSameType(object);
    }

    /**
     * Inconsistent naming (should be isSameType like in other holders)
     */
    @Deprecated(forRemoval = true)
    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    default boolean isType(Object... objects) {
        return isSameType(objects);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    static PotionEffectHolder of(Object effect) {
        return ofOptional(effect).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    static Optional<PotionEffectHolder> ofOptional(Object effect) {
        if (effect instanceof PotionEffectHolder) {
            return Optional.of((PotionEffectHolder) effect);
        }
        return PotionEffectMapping.resolve(effect);
    }

    static List<PotionEffectHolder> all() {
        return PotionEffectMapping.getValues();
    }

    @Override
    default ProtoPotionEffect asProto() {
        return ProtoPotionEffect.newBuilder()
                .setPlatformName(platformName())
                .setAmplifier(amplifier())
                .setDuration(duration())
                .setAmbient(ambient())
                .setIcon(icon())
                .setParticles(particles())
                .build();
    }
}
