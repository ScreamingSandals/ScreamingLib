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

package org.screamingsandals.lib.bungee.spectator;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bungee.spectator.event.BungeeClickEvent;
import org.screamingsandals.lib.bungee.spectator.event.BungeeHoverEvent;
import org.screamingsandals.lib.bungee.spectator.event.hover.BungeeEntityContent;
import org.screamingsandals.lib.bungee.spectator.event.hover.BungeeItemContent;
import org.screamingsandals.lib.bungee.spectator.event.hover.BungeeLegacyEntityContent;
import org.screamingsandals.lib.bungee.spectator.event.hover.BungeeLegacyItemContent;
import org.screamingsandals.lib.nbt.SNBTSerializer;
import org.screamingsandals.lib.spectator.*;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Locale;

public abstract class AbstractBungeeBackend implements SpectatorBackend {
    @Getter
    private static final @NotNull BidirectionalConverter<BungeeComponent> additionalComponentConverter = BidirectionalConverter.build();
    @Getter
    private static final @NotNull BidirectionalConverter<BungeeClickEvent> additionalClickEventConverter = BidirectionalConverter.build();
    @Getter
    private static final @NotNull BidirectionalConverter<BungeeHoverEvent> additionalHoverEventConverter = BidirectionalConverter.build();
    @Getter
    // not needed for legacy because we can never have native Adventure alongside with pre-1.16 Bungee component api
    private static final @NotNull BidirectionalConverter<BungeeEntityContent> additionalEntityContentConverter = BidirectionalConverter.build();
    @Getter
    // not needed for legacy because we can never have native Adventure alongside with pre-1.16 Bungee component api
    private static final @NotNull BidirectionalConverter<BungeeItemContent> additionalItemContentConverter = BidirectionalConverter.build();
    @Getter
    private static final @NotNull BidirectionalConverter<BungeeColor> additionalColorConverter = BidirectionalConverter.build();
    @Getter
    // We can't use NoArgsConstructor because it's too new
    private static final @NotNull Component empty = wrapComponent(new TextComponent(""));
    private static final @NotNull Component newLine = wrapComponent(new TextComponent("\n"));
    private static final @NotNull Component space = wrapComponent(new TextComponent(" "));
    @Getter
    protected static @NotNull SNBTSerializer snbtSerializer = SNBTSerializer.builder()
            .shouldSaveLongArraysDirectly(false)
            .build();
    private static final boolean keybindSupported = Reflect.has("net.md_5.bungee.api.chat.KeybindComponent");
    private static final boolean scoreSupported = Reflect.has("net.md_5.bungee.api.chat.ScoreComponent");
    private static final boolean selectorSupported = Reflect.has("net.md_5.bungee.api.chat.SelectorComponent");

    @Override
    public Component empty() {
        return empty;
    }

    @Override
    public Component newLine() {
        return newLine;
    }

    @Override
    public Component space() {
        return space;
    }

    @Override
    public BlockNBTComponent.Builder blockNBT() {
        return null; // TODO
    }

    @Override
    public EntityNBTComponent.Builder entityNBT() {
        return null; // TODO
    }

    @Override
    public KeybindComponent.Builder keybind() {
        if (keybindSupported) {
            return new BungeeKeybindComponent.BungeeKeybindBuilder(new net.md_5.bungee.api.chat.KeybindComponent(""));
        } else {
            return null; // not supported on this version
        }
    }

    @Override
    public ScoreComponent.Builder score() {
        if (scoreSupported) {
            return new BungeeScoreComponent.BungeeScoreBuilder(new net.md_5.bungee.api.chat.ScoreComponent("", ""));
        } else {
            return null; // TODO :sad:
        }
    }

    @Override
    public SelectorComponent.Builder selector() {
        if (selectorSupported) {
            return new BungeeSelectorComponent.BungeeSelectorBuilder(new net.md_5.bungee.api.chat.SelectorComponent(""));
        } else {
            return null; // TODO :cry:
        }
    }

    @Override
    public StorageNBTComponent.Builder storageNBT() {
        return null; // TODO
    }

    @Override
    public org.screamingsandals.lib.spectator.TextComponent.Builder text() {
        return new BungeeTextComponent.BungeeTextBuilder(new TextComponent(""));
    }

    @Override
    public org.screamingsandals.lib.spectator.TranslatableComponent.Builder translatable() {
        return new BungeeTranslatableContent.BungeeTranslatableBuilder(new TranslatableComponent(""));
    }

    @Override
    public Color rgb(int red, int green, int blue) {
        int combined = red << 16 | green << 8 | blue;
        try {
            return new BungeeColor(ChatColor.of(String.format("#%06X", (0xFFFFFF & combined))));
        } catch (Throwable ignored) {
            switch (combined) {
                case 0x0000AA:
                    return new BungeeColor(ChatColor.DARK_BLUE);
                case 0x00AA00:
                    return new BungeeColor(ChatColor.DARK_GREEN);
                case 0x00AAAA:
                    return new BungeeColor(ChatColor.DARK_AQUA);
                case 0xAA0000:
                    return new BungeeColor(ChatColor.DARK_RED);
                case 0xAA00AA:
                    return new BungeeColor(ChatColor.DARK_PURPLE);
                case 0xFFAA00:
                    return new BungeeColor(ChatColor.GOLD);
                case 0xAAAAAA:
                    return new BungeeColor(ChatColor.GRAY);
                case 0x555555:
                    return new BungeeColor(ChatColor.DARK_GRAY);
                case 0x5555FF:
                    return new BungeeColor(ChatColor.BLUE);
                case 0x55FF55:
                    return new BungeeColor(ChatColor.GREEN);
                case 0x55FFFF:
                    return new BungeeColor(ChatColor.AQUA);
                case 0xFF5555:
                    return new BungeeColor(ChatColor.RED);
                case 0xFF55FF:
                    return new BungeeColor(ChatColor.LIGHT_PURPLE);
                case 0xFFFF55:
                    return new BungeeColor(ChatColor.YELLOW);
                case 0xFFFFFF:
                    return new BungeeColor(ChatColor.WHITE);
                default:
                    return nearestNamedTo(red, green, blue);
            }
        }
    }

    @Override
    public Color named(String name) {
        return new BungeeColor(ChatColor.valueOf(name.toUpperCase(Locale.ROOT)));
    }

    @Override
    public Color hexOrName(String hexOrName) {
        try {
            return new BungeeColor(ChatColor.of(hexOrName.toLowerCase(Locale.ROOT)));
        } catch (Throwable ignored) {
        }
        if (hexOrName.length() == 6) {
            try {
                return new BungeeColor(ChatColor.of("#" + hexOrName.toLowerCase(Locale.ROOT)));
            } catch (Throwable ignored) {
            }
        }
        if (hexOrName.length() == 3) {
            try {
                return new BungeeColor(ChatColor.of("#" + hexOrName.charAt(0) + hexOrName.charAt(0)
                        + hexOrName.charAt(1) + hexOrName.charAt(1)
                        + hexOrName.charAt(2) + hexOrName.charAt(2)));
            } catch (Throwable ignored) {
            }
        }
        try {
            return new BungeeColor(ChatColor.valueOf(hexOrName.toUpperCase(Locale.ROOT)));
        } catch (Throwable ignored) {}
        return new BungeeColor(ChatColor.WHITE); // never returns null!!!
    }

    public Color nearestNamedTo(Color color) {
        if (color == null) {
            return Color.WHITE;
        }

        return nearestNamedTo(color.red(), color.green(), color.blue());
    }

    private Color nearestNamedTo(int red, int green, int blue) {
        var matchedDistance = Float.MAX_VALUE;
        var match = Color.WHITE;
        for (var potential : Color.NAMED_VALUES.values()) {
            final float distance = distance(rgbToHsvArray(red, green, blue), rgbToHsvArray(potential.red(), potential.green(), potential.blue()));
            if (distance < matchedDistance) {
                match = potential;
                matchedDistance = distance;
            }
            if (distance == 0) {
                break;
            }
        }
        return match;
    }

    // definitely not stolen from adventure
    private float[] rgbToHsvArray(int red, int green, int blue) {
        var r = red / 255.0f;
        var g = green / 255.0f;
        var b = blue / 255.0f;

        var min = Math.min(r, Math.min(g, b));
        var max = Math.max(r, Math.max(g, b));
        var delta = max - min;

        float s;
        if (max != 0) {
            s = delta / max;
        } else {
            s = 0;
        }
        if (s == 0) {
            return new float[] {0, s, max};
        }

        float h;
        if (r == max) {
            h = (g - b) / delta;
        } else if (g == max) {
            h = 2 + (b - r) / delta;
        } else {
            h = 4 + (r - g) / delta;
        }
        h *= 60;
        if (h < 0) {
            h += 360;
        }

        return new float[] {h / 360.0f, s, max};
    }

    // definitely not stolen from adventure
    private static float distance(float[] self, float[] other) {
        final float hueDistance = 3 * Math.min(Math.abs(self[0] - other[0]), 1f - Math.abs(self[0] - other[0]));
        final float saturationDiff = self[1] - other[1];
        final float valueDiff = self[2] - other[2];
        return hueDistance * hueDistance + saturationDiff * saturationDiff + valueDiff * valueDiff;
    }

    @Override
    public ClickEvent.Builder clickEvent() {
        return new BungeeClickEvent.BungeeClickBuilder();
    }

    @Override
    public HoverEvent.Builder hoverEvent() {
        return new BungeeHoverEvent.BungeeHoverEventBuilder();
    }

    @Override
    public EntityContent.Builder entityContent() {
        if (Reflect.has("net.md_5.bungee.api.chat.hover.content.Entity")) {
            return new BungeeEntityContent.BungeeEntityContentBuilder();
        } else {
            return new BungeeLegacyEntityContent.BungeeLegacyEntityContentBuilder();
        }
    }

    @Override
    public ItemContent.Builder itemContent() {
        if (Reflect.has("net.md_5.bungee.api.chat.hover.content.Item")) {
            return new BungeeItemContent.BungeeItemContentBuilder();
        } else {
            return new BungeeLegacyItemContent.BungeeLegacyItemContentBuilder();
        }
    }

    @Override
    public Component fromLegacy(String legacy) {
        if (legacy == null || legacy.isEmpty()) {
            return empty;
        }
        // for some reason fromLegacyText implements its own custom url handling, which is not part of the format
        var components = TextComponent.fromLegacyText(legacy);
        if (components.length == 0) {
            return empty;
        } else if (components.length == 1) {
            return wrapComponent(components[0]);
        } else {
            return wrapComponent(new TextComponent(components));
        }
    }

    @Override
    public Component fromLegacy(String legacy, char colorChar) {
        if (legacy == null || legacy.isEmpty()) {
            return empty;
        }
        if (colorChar == '§') {
            return fromLegacy(legacy);
        }
        legacy = ChatColor.translateAlternateColorCodes(colorChar, legacy);
        // for some reason fromLegacyText implements its own custom url handling, which is not part of the format
        var components = TextComponent.fromLegacyText(legacy);
        if (components.length == 0) {
            return empty;
        } else if (components.length == 1) {
            return wrapComponent(components[0]);
        } else {
            return wrapComponent(new TextComponent(components));
        }
    }

    @Override
    public Component fromJson(String json) {
        if (json == null || json.isEmpty()) {
            return empty;
        }
        var components = ComponentSerializer.parse(json);
        if (components.length == 0) {
            return empty;
        } else if (components.length == 1) {
            return wrapComponent(components[0]);
        } else {
            return wrapComponent(new TextComponent(components));
        }
    }

    @Nullable
    @Contract("null -> null; !null -> !null")
    public static Component wrapComponent(@Nullable BaseComponent component) {
        if (component == null) {
            return null;
        }

        // TODO: NBTComponent doesn't exist for some reason

        // TODO: ScoreComponent added in a3b44aa612c629955195b4697641de1b1665a587 (Feb 2018 (1.12), but existed in MC 1.8+), SelectorComponent added in the same commit
        if (scoreSupported) {
            if (component instanceof net.md_5.bungee.api.chat.ScoreComponent) {
                return new BungeeScoreComponent((net.md_5.bungee.api.chat.ScoreComponent) component);
            }
        }
        if (selectorSupported) {
            if (component instanceof net.md_5.bungee.api.chat.SelectorComponent) {
                return new BungeeSelectorComponent((net.md_5.bungee.api.chat.SelectorComponent) component);
            }
        }

        // TODO: KeybindComponent added in fbc5f514e28dbfc3f85cb936ad95b1a979086ab6 (1.12 released on June, this is from Nov of the same year)
        if (keybindSupported) {
            if (component instanceof net.md_5.bungee.api.chat.KeybindComponent) {
                return new BungeeKeybindComponent((net.md_5.bungee.api.chat.KeybindComponent) component);
            }
        }

        if (component instanceof TranslatableComponent) {
            return new BungeeTranslatableContent((TranslatableComponent) component);
        }

        if (component instanceof TextComponent) {
            return new BungeeTextComponent((TextComponent) component);
        }

        return new BungeeComponent(component);
    }
}
