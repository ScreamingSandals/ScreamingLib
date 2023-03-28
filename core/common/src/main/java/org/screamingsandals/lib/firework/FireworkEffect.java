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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.List;

public interface FireworkEffect extends RegistryItem {

    @ApiStatus.Experimental
    @NotNull String platformName();

    @NotNull List<@NotNull Color> colors();

    @NotNull List<@NotNull Color> fadeColors();

    boolean flicker();

    boolean trail();

    @Contract(value = "_ -> new", pure = true)
    @NotNull FireworkEffect withColors(@NotNull List<@NotNull Color> colors);

    @Contract(value = "_ -> new", pure = true)
    @NotNull FireworkEffect withFadeColors(@NotNull List<@NotNull Color> fadeColors);

    @Contract(value = "_ -> new", pure = true)
    @NotNull FireworkEffect withFlicker(boolean flicker);

    @Contract(value = "_ -> new", pure = true)
    @NotNull FireworkEffect withTrail(boolean trail);
    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    boolean is(@Nullable Object @NotNull... objects);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    boolean is(@Nullable Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    static @NotNull FireworkEffect of(@NotNull Object effect) {
        var result = ofNullable(effect);
        Preconditions.checkNotNullIllegal(result, "Could not find firework effect: " + effect);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    @Contract("null -> null")
    static @Nullable FireworkEffect ofNullable(@Nullable Object effect) {
        if (effect instanceof FireworkEffect) {
            return (FireworkEffect) effect;
        }
        return FireworkEffectRegistry.getInstance().resolveMapping(effect);
    }

    static @NotNull RegistryItemStream<@NotNull FireworkEffect> all() {
        return FireworkEffectRegistry.getInstance().getRegistryItemStream();
    }
}
