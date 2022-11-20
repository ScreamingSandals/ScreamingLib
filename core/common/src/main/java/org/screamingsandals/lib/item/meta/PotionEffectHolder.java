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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;

@SuppressWarnings("AlternativeMethodAvailable")
public interface PotionEffectHolder extends ComparableWrapper {
    String platformName();

    int duration();

    int amplifier();

    boolean ambient();

    boolean particles();

    boolean icon();

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffectHolder withDuration(int duration);

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffectHolder withAmplifier(int amplifier);

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffectHolder withAmbient(boolean ambient);

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffectHolder withParticles(boolean particles);

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffectHolder withIcon(boolean icon);

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
    static @NotNull PotionEffectHolder of(@NotNull Object effect) {
        var result = ofNullable(effect);
        Preconditions.checkNotNullIllegal(result, "Could not find potion effect: " + effect);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    @Contract("null -> null")
    static @Nullable PotionEffectHolder ofNullable(@Nullable Object effect) {
        if (effect instanceof PotionEffectHolder) {
            return (PotionEffectHolder) effect;
        }
        return PotionEffectMapping.resolve(effect);
    }

    static @NotNull List<@NotNull PotionEffectHolder> all() {
        return PotionEffectMapping.getValues();
    }
}
