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

package org.screamingsandals.lib.firework;

import org.jetbrains.annotations.Contract;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface FireworkEffectHolder extends ComparableWrapper {

    String platformName();

    List<Color> colors();

    List<Color> fadeColors();

    boolean flicker();

    boolean trail();

    @Contract(value = "_ -> new", pure = true)
    FireworkEffectHolder withColors(List<Color> colors);

    @Contract(value = "_ -> new", pure = true)
    FireworkEffectHolder withFadeColors(List<Color> fadeColors);

    @Contract(value = "_ -> new", pure = true)
    FireworkEffectHolder withFlicker(boolean flicker);

    @Contract(value = "_ -> new", pure = true)
    FireworkEffectHolder withTrail(boolean trail);

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
    default List<Color> getColors() {
        return colors();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default List<Color> getFadeColors() {
        return fadeColors();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default boolean isFlicker() {
        return flicker();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default boolean isTrail() {
        return trail();
    }

    /**
     * Inconsistent naming (holders use with prefix)
     */
    @Deprecated(forRemoval = true)
    default FireworkEffectHolder colors(List<Color> colors) {
        return withColors(colors);
    }

    /**
     * Inconsistent naming (holders use with prefix)
     */
    @Deprecated(forRemoval = true)
    default FireworkEffectHolder fadeColors(List<Color> fadeColors) {
        return withFadeColors(fadeColors);
    }

    /**
     * Inconsistent naming (holders use with prefix)
     */
    @Deprecated(forRemoval = true)
    default FireworkEffectHolder flicker(boolean flicker) {
        return withFlicker(flicker);
    }

    /**
     * Inconsistent naming (holders use with prefix)
     */
    @Deprecated(forRemoval = true)
    default FireworkEffectHolder trail(boolean trail) {
        return withTrail(trail);
    }

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    boolean is(Object... objects);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    static FireworkEffectHolder of(Object effect) {
        return ofOptional(effect).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    static Optional<FireworkEffectHolder> ofOptional(Object effect) {
        if (effect instanceof FireworkEffectHolder) {
            return Optional.of((FireworkEffectHolder) effect);
        }
        return FireworkEffectMapping.resolve(effect);
    }

    static List<FireworkEffectHolder> all() {
        return FireworkEffectMapping.getValues();
    }
}
