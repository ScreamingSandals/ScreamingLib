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

package org.screamingsandals.lib.firework;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;

public interface FireworkEffectHolder extends ComparableWrapper {

    @NotNull String platformName();

    @NotNull List<@NotNull Color> colors();

    @NotNull List<@NotNull Color> fadeColors();

    boolean flicker();

    boolean trail();

    @Contract(value = "_ -> new", pure = true)
    @NotNull FireworkEffectHolder withColors(@NotNull List<@NotNull Color> colors);

    @Contract(value = "_ -> new", pure = true)
    @NotNull FireworkEffectHolder withFadeColors(@NotNull List<@NotNull Color> fadeColors);

    @Contract(value = "_ -> new", pure = true)
    @NotNull FireworkEffectHolder withFlicker(boolean flicker);

    @Contract(value = "_ -> new", pure = true)
    @NotNull FireworkEffectHolder withTrail(boolean trail);
    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    boolean is(@Nullable Object @NotNull... objects);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    boolean is(@Nullable Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    static @NotNull FireworkEffectHolder of(@NotNull Object effect) {
        var result = ofNullable(effect);
        Preconditions.checkNotNullIllegal(result, "Could not find firework effect: " + effect);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    @Contract("null -> null")
    static @Nullable FireworkEffectHolder ofNullable(@Nullable Object effect) {
        if (effect instanceof FireworkEffectHolder) {
            return (FireworkEffectHolder) effect;
        }
        return FireworkEffectMapping.resolve(effect);
    }

    static @Unmodifiable @NotNull List<@NotNull FireworkEffectHolder> all() {
        return FireworkEffectMapping.getValues();
    }
}
