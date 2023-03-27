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

package org.screamingsandals.lib.item.meta;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface PotionEffect extends RegistryItem {
    @NotNull String platformName();

    int duration();

    int amplifier();

    boolean ambient();

    boolean particles();

    boolean icon();

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffect withDuration(int duration);

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffect withAmplifier(int amplifier);

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffect withAmbient(boolean ambient);

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffect withParticles(boolean particles);

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffect withIcon(boolean icon);

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    @Override
    boolean is(@Nullable Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    @Override
    boolean is(@Nullable Object @NotNull... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    boolean isSameType(@Nullable Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    boolean isSameType(@Nullable Object @NotNull... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    static @NotNull PotionEffect of(@NotNull Object effect) {
        var result = ofNullable(effect);
        Preconditions.checkNotNullIllegal(result, "Could not find potion effect: " + effect);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    @Contract("null -> null")
    static @Nullable PotionEffect ofNullable(@Nullable Object effect) {
        if (effect instanceof PotionEffect) {
            return (PotionEffect) effect;
        }
        return PotionEffectRegistry.getInstance().resolveMapping(effect);
    }

    static @NotNull RegistryItemStream<@NotNull PotionEffect> all() {
        return PotionEffectRegistry.getInstance().getRegistryItemStream();
    }
}
