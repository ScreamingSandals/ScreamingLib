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

package org.screamingsandals.lib.impl.bungee.spectator;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.utils.BasicWrapper;

public class BungeeColor extends BasicWrapper<ChatColor> implements Color {
    protected BungeeColor(@NotNull ChatColor wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int red() {
        if (BungeeChatFeature.RGB_COLORS.isSupported()) {
            return wrappedObject.getColor().getRed();
        } else {
            // Pre 1.16 Bungee Chat API (it was an enum, but javac is sus, at least the order was consistent)
            switch (wrappedObject.ordinal()) {
                case 0: // BLACK
                    return 0;
                case 1: // DARK_BLUE
                    return 0;
                case 2: // DARK_GREEN
                    return 0;
                case 3: // DARK_AQUA
                    return 0;
                case 4: // DARK_RED
                    return 0xAA;
                case 5: // DARK_PURPLE
                    return 0xAA;
                case 6: // GOLD
                    return 0xFF;
                case 7: // GRAY
                    return 0xAA;
                case 8: // DARK_GRAY
                    return 0x55;
                case 9: // BLUE
                    return 0x55;
                case 10: // GREEN
                    return 0x55;
                case 11: // AQUA
                    return 0x55;
                case 12: // RED
                    return 0xFF;
                case 13: // LIGHT_PURPLE
                    return 0xFF;
                case 14: // YELLOW
                    return 0xFF;
                case 15: // WHITE
                    return 0xFF;
            }
            return 0;
        }
    }

    @Override
    public int green() {
        if (BungeeChatFeature.RGB_COLORS.isSupported()) {
            return wrappedObject.getColor().getGreen();
        } else {
            // Pre 1.16 Bungee Chat API (it was an enum, but javac is sus, at least the order was consistent)
            switch (wrappedObject.ordinal()) {
                case 0: // BLACK
                    return 0;
                case 1: // DARK_BLUE
                    return 0;
                case 2: // DARK_GREEN
                    return 0xAA;
                case 3: // DARK_AQUA
                    return 0xAA;
                case 4: // DARK_RED
                    return 0x00;
                case 5: // DARK_PURPLE
                    return 0x00;
                case 6: // GOLD
                    return 0xAA;
                case 7: // GRAY
                    return 0xAA;
                case 8: // DARK_GRAY
                    return 0x55;
                case 9: // BLUE
                    return 0x55;
                case 10: // GREEN
                    return 0xFF;
                case 11: // AQUA
                    return 0xFF;
                case 12: // RED
                    return 0x55;
                case 13: // LIGHT_PURPLE
                    return 0x55;
                case 14: // YELLOW
                    return 0xFF;
                case 15: // WHITE
                    return 0xFF;
            }
            return 0;
        }
    }

    @Override
    public int blue() {
        if (BungeeChatFeature.RGB_COLORS.isSupported()) {
            return wrappedObject.getColor().getBlue();
        } else {
            // Pre 1.16 Bungee Chat API (it was an enum, but javac is sus, at least the order was consistent)
            switch (wrappedObject.ordinal()) {
                case 0: // BLACK
                    return 0;
                case 1: // DARK_BLUE
                    return 0xAA;
                case 2: // DARK_GREEN
                    return 0;
                case 3: // DARK_AQUA
                    return 0xAA;
                case 4: // DARK_RED
                    return 0;
                case 5: // DARK_PURPLE
                    return 0xAA;
                case 6: // GOLD
                    return 0;
                case 7: // GRAY
                    return 0xAA;
                case 8: // DARK_GRAY
                    return 0x55;
                case 9: // BLUE
                    return 0xFF;
                case 10: // GREEN
                    return 0x55;
                case 11: // AQUA
                    return 0xFF;
                case 12: // RED
                    return 0x55;
                case 13: // LIGHT_PURPLE
                    return 0xFF;
                case 14: // YELLOW
                    return 0x55;
                case 15: // WHITE
                    return 0xFF;
            }
            return 0;
        }
    }

    @Override
    public @NotNull String toString() {
        return wrappedObject.name();
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AbstractBungeeBackend.getAdditionalColorConverter().convert(this, type);
        }
    }
}
