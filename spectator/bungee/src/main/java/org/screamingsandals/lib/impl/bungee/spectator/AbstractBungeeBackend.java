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

package org.screamingsandals.lib.impl.bungee.spectator;

import com.google.gson.Gson;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bungee.spectator.backports.*;
import org.screamingsandals.lib.impl.bungee.spectator.event.BungeeClickEvent;
import org.screamingsandals.lib.impl.bungee.spectator.event.BungeeHoverEvent;
import org.screamingsandals.lib.impl.bungee.spectator.event.hover.BungeeEntityContent;
import org.screamingsandals.lib.impl.bungee.spectator.event.hover.BungeeItemContent;
import org.screamingsandals.lib.impl.bungee.spectator.event.hover.BungeeLegacyEntityContent;
import org.screamingsandals.lib.impl.bungee.spectator.event.hover.BungeeLegacyItemContent;
import org.screamingsandals.lib.impl.spectator.SpectatorBackend;
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

    static final boolean COMPONENTS_PORTED_SUCCESSFULLY;

    static {
        var gson = Reflect.getField(ComponentSerializer.class, "gson");
        if (gson instanceof Gson) {
            COMPONENTS_PORTED_SUCCESSFULLY = Injector.injectGson((Gson) gson, gsonBuilder -> gsonBuilder.registerTypeHierarchyAdapter(BasePortedComponent.class, new PortedComponentSerializer()));
        } else {
            COMPONENTS_PORTED_SUCCESSFULLY = false;
        }
    }

    @Override
    public @NotNull Component empty() {
        return empty;
    }

    @Override
    public @NotNull Component newLine() {
        return newLine;
    }

    @Override
    public @NotNull Component space() {
        return space;
    }

    @Override
    public @Nullable Component fromPlatform(@NotNull Object platformObject) {
        if (platformObject instanceof Component) {
            return (Component) platformObject;
        } else if (platformObject instanceof BaseComponent) {
            return wrapComponent((BaseComponent) platformObject);
        }
        throw new IllegalArgumentException("Not possible to convert unknown object type to Component: " + platformObject);
    }

    @Override
    public BlockNBTComponent.@NotNull Builder blockNBT() {
        if (COMPONENTS_PORTED_SUCCESSFULLY) {
            return new PortedBungeeBlockNBTComponent.BungeeBlockNBTBuilder(new BlockNBTPortedComponent());
        } else {
            throw new UnsupportedOperationException("Not implemented for md_5 ChatComponent API!");
        }
    }

    @Override
    public EntityNBTComponent.@NotNull Builder entityNBT() {
        if (COMPONENTS_PORTED_SUCCESSFULLY) {
            return new PortedBungeeEntityNBTComponent.BungeeEntityNBTBuilder(new EntityNBTPortedComponent());
        } else {
            throw new UnsupportedOperationException("Not implemented for md_5 ChatComponent API!");
        }
    }

    @Override
    public KeybindComponent.@NotNull Builder keybind() {
        if (BungeeChatFeature.KEYBIND_COMPONENT.isSupported()) {
            return new BungeeKeybindComponent.BungeeKeybindBuilder(new net.md_5.bungee.api.chat.KeybindComponent(""));
        } else {
            throw new UnsupportedOperationException("Not supported on this version of md_5 ChatComponent API!"); // TODO: not supported on this version
        }
    }

    @Override
    public ScoreComponent.@NotNull Builder score() {
        if (BungeeChatFeature.SCORE_COMPONENT.isSupported()) {
            return new BungeeScoreComponent.BungeeScoreBuilder(new net.md_5.bungee.api.chat.ScoreComponent("", ""));
        } else if (COMPONENTS_PORTED_SUCCESSFULLY) {
            return new PortedBungeeScoreComponent.BungeeScoreBuilder(new ScorePortedComponent("", ""));
        } else {
            throw new UnsupportedOperationException("Not implemented for this version of md_5 ChatComponent API!");
        }
    }

    @Override
    public SelectorComponent.@NotNull Builder selector() {
        if (BungeeChatFeature.SELECTOR_COMPONENT.isSupported()) {
            if (COMPONENTS_PORTED_SUCCESSFULLY) {
                // We prefer using the original component, but because it does not support everything, we may need to switch the underlying component to our own
                return new BungeeSelectorComponent.MultipleImplementationsBuilder(new net.md_5.bungee.api.chat.SelectorComponent(""));
            }
            return new BungeeSelectorComponent.BungeeSelectorBuilder(new net.md_5.bungee.api.chat.SelectorComponent(""));
        } else if (COMPONENTS_PORTED_SUCCESSFULLY) {
            return new PortedBungeeSelectorComponent.BungeeSelectorBuilder(new SelectorPortedComponent(""));
        } else {
            throw new UnsupportedOperationException("Not implemented for this version of md_5 ChatComponent API yet!");
        }
    }

    @Override
    public StorageNBTComponent.@NotNull Builder storageNBT() {
        if (COMPONENTS_PORTED_SUCCESSFULLY) {
            return new PortedBungeeStorageNBTComponent.BungeeStorageNBTBuilder(new StorageNBTPortedComponent());
        } else {
            throw new UnsupportedOperationException("Not implemented for md_5 ChatComponent API!");
        }
    }

    @Override
    public org.screamingsandals.lib.spectator.TextComponent.@NotNull Builder text() {
        return new BungeeTextComponent.BungeeTextBuilder(new TextComponent(""));
    }

    @Override
    public org.screamingsandals.lib.spectator.TranslatableComponent.@NotNull Builder translatable() {
        return new BungeeTranslatableContent.BungeeTranslatableBuilder(new TranslatableComponent(""));
    }

    @Override
    public @NotNull Color rgb(int red, int green, int blue) {
        int combined = red << 16 | green << 8 | blue;
        if (BungeeChatFeature.RGB_COLORS.isSupported()) {
            return new BungeeColor(ChatColor.of(String.format("#%06X", (0xFFFFFF & combined))));
        } else {
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
    public @Nullable Color named(@NotNull String name) {
        // bri'ish
        if ("grey".equalsIgnoreCase(name)) {
            name = "gray";
        } else if ("dark_grey".equalsIgnoreCase(name)) {
            name = "dark_gray";
        }

        try {
            return new BungeeColor(ChatColor.valueOf(name.toUpperCase(Locale.ROOT)));
        } catch (Throwable ignored) {
            return null;
        }
    }

    @Override
    public @NotNull Color hexOrName(@NotNull String hexOrName) {
        if (BungeeChatFeature.RGB_COLORS.isSupported()) {
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
        }
        try {
            return new BungeeColor(ChatColor.valueOf(hexOrName.toUpperCase(Locale.ROOT)));
        } catch (Throwable ignored) {}
        return new BungeeColor(ChatColor.WHITE); // never returns null!!!
    }

    public @NotNull Color nearestNamedTo(@Nullable Color color) {
        if (color == null) {
            return Color.WHITE;
        }

        return nearestNamedTo(color.red(), color.green(), color.blue());
    }

    private @NotNull Color nearestNamedTo(int red, int green, int blue) {
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
            return new float[] {0, 0, max};
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
    public ClickEvent.@NotNull Builder clickEvent() {
        return new BungeeClickEvent.BungeeClickBuilder();
    }

    @Override
    public HoverEvent.@NotNull Builder hoverEvent() {
        return new BungeeHoverEvent.BungeeHoverEventBuilder();
    }

    @Override
    public EntityContent.@NotNull Builder entityContent() {
        if (BungeeChatFeature.MODERN_HOVER_CONTENTS.isSupported()) {
            return new BungeeEntityContent.BungeeEntityContentBuilder();
        } else {
            return new BungeeLegacyEntityContent.BungeeLegacyEntityContentBuilder();
        }
    }

    @Override
    public ItemContent.@NotNull Builder itemContent() {
        if (BungeeChatFeature.MODERN_HOVER_CONTENTS.isSupported()) {
            return new BungeeItemContent.BungeeItemContentBuilder();
        } else {
            return new BungeeLegacyItemContent.BungeeLegacyItemContentBuilder();
        }
    }

    @Override
    public @NotNull Component fromLegacy(@NotNull String legacy) {
        if (legacy.isEmpty()) {
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
    public @NotNull Component fromLegacy(@NotNull String legacy, char colorChar) {
        if (legacy.isEmpty()) {
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
    public @NotNull Component fromJson(@NotNull String json) {
        if (json.isEmpty()) {
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

    @Contract("null -> null; !null -> !null")
    public static @Nullable Component wrapComponent(@Nullable BaseComponent component) {
        if (component == null) {
            return null;
        }

        // ported component, sadly this way we can only detect our own, we are unable to properly wrap custom components of different plugins
        if (component instanceof BlockNBTPortedComponent) {
            return new PortedBungeeBlockNBTComponent((BlockNBTPortedComponent) component);
        }

        if (component instanceof EntityNBTPortedComponent) {
            return new PortedBungeeEntityNBTComponent((EntityNBTPortedComponent) component);
        }

        if (component instanceof StorageNBTPortedComponent) {
            return new PortedBungeeStorageNBTComponent((StorageNBTPortedComponent) component);
        }

        if (component instanceof SelectorPortedComponent) {
            return new PortedBungeeSelectorComponent((SelectorPortedComponent) component);
        }

        if (component instanceof ScorePortedComponent) {
            return new PortedBungeeScoreComponent((ScorePortedComponent) component);
        }

        // native components

        // there are no native components yet for nbt tags

        // ScoreComponent added in a3b44aa612c629955195b4697641de1b1665a587 (Feb 2018 (1.12), but existed in MC 1.8+), SelectorComponent added in the same commit
        if (BungeeChatFeature.SCORE_COMPONENT.isSupported()) {
            if (component instanceof net.md_5.bungee.api.chat.ScoreComponent) {
                return new BungeeScoreComponent((net.md_5.bungee.api.chat.ScoreComponent) component);
            }
        }
        if (BungeeChatFeature.SELECTOR_COMPONENT.isSupported()) {
            if (component instanceof net.md_5.bungee.api.chat.SelectorComponent) {
                return new BungeeSelectorComponent((net.md_5.bungee.api.chat.SelectorComponent) component);
            }
        }

        // KeybindComponent added in fbc5f514e28dbfc3f85cb936ad95b1a979086ab6 (1.12 released on June, this is from Nov of the same year)
        // TODO: is it worth to backport it? if yes, we probably also want to replace it with text component for versions older than 1.12
        if (BungeeChatFeature.KEYBIND_COMPONENT.isSupported()) {
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
