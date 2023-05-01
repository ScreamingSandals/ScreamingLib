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

package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.spectator.ColorLink;
import org.screamingsandals.lib.impl.spectator.Spectator;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.api.Wrapper;

import java.util.Map;

public interface Color extends Wrapper, ComponentBuilderApplicable, RawValueHolder {
    @NotNull Color BLACK = new ColorLink("BLACK");
    @NotNull Color DARK_BLUE = new ColorLink("DARK_BLUE");
    @NotNull Color DARK_GREEN = new ColorLink("DARK_GREEN");
    @NotNull Color DARK_AQUA = new ColorLink("DARK_AQUA");
    @NotNull Color DARK_RED = new ColorLink("DARK_RED");
    @NotNull Color DARK_PURPLE = new ColorLink("DARK_PURPLE");
    @NotNull Color GOLD = new ColorLink("GOLD");
    @NotNull Color GRAY = new ColorLink("GRAY");
    @NotNull Color DARK_GRAY = new ColorLink("DARK_GRAY");
    @NotNull Color BLUE = new ColorLink("BLUE");
    @NotNull Color GREEN = new ColorLink("GREEN");
    @NotNull Color AQUA = new ColorLink("AQUA");
    @NotNull Color RED = new ColorLink("RED");
    @NotNull Color LIGHT_PURPLE = new ColorLink("LIGHT_PURPLE");
    @NotNull Color YELLOW = new ColorLink("YELLOW");
    @NotNull Color WHITE = new ColorLink("WHITE");

    @NotNull Map<@NotNull String, Color> NAMED_VALUES = Map.ofEntries(
            Map.entry("black", BLACK),
            Map.entry("dark_blue", DARK_BLUE),
            Map.entry("dark_green", DARK_GREEN),
            Map.entry("dark_aqua", DARK_AQUA),
            Map.entry("dark_red", DARK_RED),
            Map.entry("dark_purple", DARK_PURPLE),
            Map.entry("gold", GOLD),
            Map.entry("gray", GRAY),
            Map.entry("dark_gray", DARK_GRAY),
            Map.entry("blue", BLUE),
            Map.entry("green", GREEN),
            Map.entry("aqua", AQUA),
            Map.entry("red", RED),
            Map.entry("light_purple", LIGHT_PURPLE),
            Map.entry("yellow", YELLOW),
            Map.entry("white", WHITE)
    );

    int red();

    int green();

    int blue();

    default int compoundRgb() {
        return red() << 16 | green() << 8 | blue();
    }

    @NotNull String toString();

    static @NotNull Color rgb(int red, int green, int blue) {
        return Spectator.getBackend().rgb(red, green, blue);
    }

    static @Nullable Color named(@NotNull String name) {
        return Spectator.getBackend().named(name);
    }

    static @NotNull Color hexOrName(@NotNull String hex) {
        return Spectator.getBackend().hexOrName(hex);
    }

    static @NotNull Color nearestNamedTo(@Nullable Color color) {
        return Spectator.getBackend().nearestNamedTo(color);
    }

    static @NotNull Color interpolate(float t, final @NotNull Color a, @NotNull Color b) {
        float clampedT = Math.min(1.0f, Math.max(0.0f, t));
        int ar = a.red();
        int br = b.red();
        int ag = a.green();
        int bg = b.green();
        int ab = a.blue();
        int bb = b.blue();
        return rgb(
                Math.round(ar + clampedT * (br - ar)),
                Math.round(ag + clampedT * (bg - ag)),
                Math.round(ab + clampedT * (bb - ab))
        );
    }

    @Override
    default <C extends Component, B extends Component.Builder<B, C>> void apply(@NotNull B builder) {
        builder.color(this);
    }

    @Override
    default @NotNull Component applyTo(@NotNull Component component) {
        return component.withColor(this);
    }
}
