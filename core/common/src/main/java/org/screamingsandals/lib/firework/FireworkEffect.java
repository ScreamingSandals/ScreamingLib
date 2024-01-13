/*
 * Copyright 2024 ScreamingSandals
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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.firework.FireworkEffectRegistry;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;

import java.util.List;

public interface FireworkEffect extends ComparableWrapper, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    @NotNull FireworkEffectType type();

    @NotNull List<@NotNull Color> colors();

    @NotNull List<@NotNull Color> fadeColors();

    boolean flicker();

    boolean trail();

    @Contract(value = "_ -> new", pure = true)
    @NotNull FireworkEffect withType(@NotNull FireworkEffectType type);

    @Contract(value = "_ -> new", pure = true)
    @NotNull FireworkEffect withColors(@NotNull List<@NotNull Color> colors);

    @Contract(value = "_ -> new", pure = true)
    @NotNull FireworkEffect withFadeColors(@NotNull List<@NotNull Color> fadeColors);

    @Contract(value = "_ -> new", pure = true)
    @NotNull FireworkEffect withFlicker(boolean flicker);

    @Contract(value = "_ -> new", pure = true)
    @NotNull FireworkEffect withTrail(boolean trail);
    @Override
    boolean is(@MinecraftType(MinecraftType.Type.FIREWORK_EFFECT) @Nullable Object @NotNull... objects);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.FIREWORK_EFFECT) @Nullable Object object);

    static @NotNull FireworkEffect of(@MinecraftType(MinecraftType.Type.FIREWORK_EFFECT_OR_FIREWORK_EFFECT_TYPE) @NotNull Object effect) {
        var result = ofNullable(effect);
        Preconditions.checkNotNullIllegal(result, "Could not find firework effect: " + effect);
        return result;
    }

    @Contract("null -> null")
    static @Nullable FireworkEffect ofNullable(@MinecraftType(MinecraftType.Type.FIREWORK_EFFECT_OR_FIREWORK_EFFECT_TYPE) @Nullable Object effect) {
        if (effect instanceof FireworkEffect) {
            return (FireworkEffect) effect;
        }
        return FireworkEffectRegistry.resolve(effect);
    }
}
