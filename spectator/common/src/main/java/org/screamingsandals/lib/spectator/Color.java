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

import java.util.Locale;
import java.util.Map;

public interface Color extends Wrapper, ComponentBuilderApplicable, RawValueHolder {
    @NotNull Color BLACK = new ColorLink.Named("BLACK");
    @NotNull Color DARK_BLUE = new ColorLink.Named("DARK_BLUE");
    @NotNull Color DARK_GREEN = new ColorLink.Named("DARK_GREEN");
    @NotNull Color DARK_AQUA = new ColorLink.Named("DARK_AQUA");
    @NotNull Color DARK_RED = new ColorLink.Named("DARK_RED");
    @NotNull Color DARK_PURPLE = new ColorLink.Named("DARK_PURPLE");
    @NotNull Color GOLD = new ColorLink.Named("GOLD");
    @NotNull Color GRAY = new ColorLink.Named("GRAY");
    @NotNull Color DARK_GRAY = new ColorLink.Named("DARK_GRAY");
    @NotNull Color BLUE = new ColorLink.Named("BLUE");
    @NotNull Color GREEN = new ColorLink.Named("GREEN");
    @NotNull Color AQUA = new ColorLink.Named("AQUA");
    @NotNull Color RED = new ColorLink.Named("RED");
    @NotNull Color LIGHT_PURPLE = new ColorLink.Named("LIGHT_PURPLE");
    @NotNull Color YELLOW = new ColorLink.Named("YELLOW");
    @NotNull Color WHITE = new ColorLink.Named("WHITE");

    // New Bedrock colors
    @NotNull Color MINECOIN_GOLD = new ColorLink.RGB(0xDD, 0xD6, 0x05);
    @NotNull Color MATERIAL_QUARTZ = new ColorLink.RGB(0xE3, 0xD4, 0xD1);
    @NotNull Color MATERIAL_IRON = new ColorLink.RGB(0xCE, 0xCA, 0xCA);
    @NotNull Color MATERIAL_NETHERITE = new ColorLink.RGB(0x44, 0x3A, 0x3B);
    @NotNull Color MATERIAL_REDSTONE = new ColorLink.RGB(0x97, 0x16, 0x07);
    @NotNull Color MATERIAL_COPPER = new ColorLink.RGB(0xB4, 0x68, 0x4D);
    @NotNull Color MATERIAL_GOLD = new ColorLink.RGB(0xDE, 0xB1, 0x2D);
    @NotNull Color MATERIAL_EMERALD = new ColorLink.RGB(0x47, 0xA0, 0x36);
    @NotNull Color MATERIAL_DIAMOND = new ColorLink.RGB(0x2C, 0xBA, 0xA8);
    @NotNull Color MATERIAL_LAPIS = new ColorLink.RGB(0x21, 0x49, 0x7B);
    @NotNull Color MATERIAL_AMETHYST = new ColorLink.RGB(0x9A, 0x5C, 0xC6);

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

    @NotNull Map<@NotNull String, Color> EXTENDED_NAMED_VALUES = Map.ofEntries(
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
            Map.entry("white", WHITE),

            // New Bedrock colors
            Map.entry("minecoin_gold", MINECOIN_GOLD),
            Map.entry("material_quartz", MATERIAL_QUARTZ),
            Map.entry("material_iron", MATERIAL_IRON),
            Map.entry("material_netherite", MATERIAL_NETHERITE),
            Map.entry("material_redstone", MATERIAL_REDSTONE),
            Map.entry("material_copper", MATERIAL_COPPER),
            Map.entry("material_gold", MATERIAL_GOLD),
            Map.entry("material_emerald", MATERIAL_EMERALD),
            Map.entry("material_diamond", MATERIAL_DIAMOND),
            Map.entry("material_lapis", MATERIAL_LAPIS),
            Map.entry("material_amethyst", MATERIAL_AMETHYST)
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
        // bri'ish
        if ("grey".equalsIgnoreCase(name)) {
            name = "gray";
        } else if ("dark_grey".equalsIgnoreCase(name)) {
            name = "dark_gray";
        }

        return Color.EXTENDED_NAMED_VALUES.get(name.toLowerCase(Locale.ROOT)); // support for new bedrock colors
    }

    static @NotNull Color hexOrName(@NotNull String hex) {
        // bri'ish
        if ("grey".equalsIgnoreCase(hex)) {
            hex = "gray";
        } else if ("dark_grey".equalsIgnoreCase(hex)) {
            hex = "dark_gray";
        }

        var value = Color.EXTENDED_NAMED_VALUES.get(hex.toLowerCase(Locale.ROOT));
        if (value != null) {
            return value; // support for new bedrock colors
        }
        return Spectator.getBackend().hexOrName(hex);
    }

    static @NotNull Color nearestNamedTo(@Nullable Color color) {
        return Spectator.getBackend().nearestNamedTo(color); // nearest named must not use EXTENDED_NAMED_VALUES to work correctly with legacy Java
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
