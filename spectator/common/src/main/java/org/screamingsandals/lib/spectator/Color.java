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

package org.screamingsandals.lib.spectator;

import org.screamingsandals.lib.utils.Wrapper;

import java.util.Map;

public interface Color extends Wrapper, ComponentBuilderApplicable {
    Color BLACK = new ColorLink("BLACK");
    Color DARK_BLUE = new ColorLink("DARK_BLUE");
    Color DARK_GREEN = new ColorLink("DARK_GREEN");;
    Color DARK_AQUA = new ColorLink("DARK_AQUA");
    Color DARK_RED = new ColorLink("DARK_RED");
    Color DARK_PURPLE = new ColorLink("DARK_PURPLE");
    Color GOLD = new ColorLink("GOLD");
    Color GRAY = new ColorLink("GRAY");
    Color DARK_GRAY = new ColorLink("DARK_GRAY");
    Color BLUE = new ColorLink("BLUE");
    Color GREEN = new ColorLink("GREEN");
    Color AQUA = new ColorLink("AQUA");
    Color RED = new ColorLink("RED");
    Color LIGHT_PURPLE = new ColorLink("LIGHT_PURPLE");
    Color YELLOW = new ColorLink("YELLOW");
    Color WHITE = new ColorLink("WHITE");

    Map<String, Color> NAMED_VALUES = Map.ofEntries(
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

    String toString();

    static Color rgb(int red, int green, int blue) {
        return Spectator.getBackend().rgb(red, green, blue);
    }

    static Color named(String name) {
        return Spectator.getBackend().named(name);
    }

    static Color hexOrName(String hex) {
        return Spectator.getBackend().hexOrName(hex);
    }

    static Color nearestNamedTo(Color color) {
        return Spectator.getBackend().nearestNamedTo(color);
    }

    @Override
    default <C extends Component, B extends Component.Builder<B, C>> void apply(B builder) {
        builder.color(this);
    }

    @Override
    default Component applyTo(Component component) {
        return component.withColor(this);
    }
}
