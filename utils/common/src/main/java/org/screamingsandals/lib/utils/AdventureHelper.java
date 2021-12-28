package org.screamingsandals.lib.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@UtilityClass
public class AdventureHelper {
    @Getter
    private final static LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER;
    @Getter
    private final static GsonComponentSerializer GSON_COMPONENT_SERIALIZER;

    public static final Map<NamedTextColor, Integer> NAMED_TEXT_COLOR_ID_MAP = Map.ofEntries(
            Map.entry(NamedTextColor.BLACK, 0),
            Map.entry(NamedTextColor.DARK_BLUE, 1),
            Map.entry(NamedTextColor.DARK_GREEN, 2),
            Map.entry(NamedTextColor.DARK_AQUA, 3),
            Map.entry(NamedTextColor.DARK_RED, 4),
            Map.entry(NamedTextColor.DARK_PURPLE, 5),
            Map.entry(NamedTextColor.GOLD, 6),
            Map.entry(NamedTextColor.GRAY, 7),
            Map.entry(NamedTextColor.DARK_GRAY, 8),
            Map.entry(NamedTextColor.BLUE, 9),
            Map.entry(NamedTextColor.GREEN, 10),
            Map.entry(NamedTextColor.AQUA, 11),
            Map.entry(NamedTextColor.RED, 12),
            Map.entry(NamedTextColor.LIGHT_PURPLE, 13),
            Map.entry(NamedTextColor.YELLOW, 14),
            Map.entry(NamedTextColor.WHITE, 15)
    );


    static {
        LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
                .hexColors()
                .useUnusualXRepeatedCharacterHexFormat()
                .character(LegacyComponentSerializer.SECTION_CHAR)
                .build();
        GSON_COMPONENT_SERIALIZER = GsonComponentSerializer.gson();
    }

    @NotNull
    public String toJson(@NotNull Component component) {
        return GSON_COMPONENT_SERIALIZER.serialize(component);
    }

    @NotNull
    public String toJson(@NotNull ComponentLike component) {
        return GSON_COMPONENT_SERIALIZER.serialize(component.asComponent());
    }

    @NotNull
    public String toLegacy(@NotNull Component component) {
        return LEGACY_COMPONENT_SERIALIZER.serialize(component);
    }

    @NotNull
    public String toLegacy(@NotNull ComponentLike component) {
        return LEGACY_COMPONENT_SERIALIZER.serialize(component.asComponent());
    }

    @NotNull
    public String toLegacyNullable(@Nullable Component component) {
        return component == null ? "" : LEGACY_COMPONENT_SERIALIZER.serialize(component);
    }

    @NotNull
    public String toLegacyNullable(@Nullable ComponentLike component) {
        return component == null ? "" : LEGACY_COMPONENT_SERIALIZER.serialize(component.asComponent());
    }

    @Nullable
    public String toLegacyNullableResult(@Nullable Component component) {
        return component == null ? null : LEGACY_COMPONENT_SERIALIZER.serialize(component);
    }

    @Nullable
    public String toLegacyNullableResult(@Nullable ComponentLike component) {
        return component == null ? null : LEGACY_COMPONENT_SERIALIZER.serialize(component.asComponent());
    }

    @NotNull
    public TextComponent toComponent(@NotNull String input) {
        return LEGACY_COMPONENT_SERIALIZER.deserialize(input);
    }

    @NotNull
    public TextComponent toComponentNullable(@Nullable String input) {
        return input == null ? Component.empty() : LEGACY_COMPONENT_SERIALIZER.deserialize(input);
    }

    @Nullable
    public TextComponent toComponentNullableResult(@Nullable String input) {
        return input == null ? null : LEGACY_COMPONENT_SERIALIZER.deserialize(input);
    }

    @NotNull
    public String translateAlternateColorCodes(char altColorChar, @NotNull String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
                b[i] = LegacyComponentSerializer.SECTION_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    public String toLegacyColorCode(TextColor color) {
        return LegacyComponentSerializer.SECTION_CHAR + Integer.toString(NAMED_TEXT_COLOR_ID_MAP.get(NamedTextColor.nearestTo(color)), 16);
    }
}
