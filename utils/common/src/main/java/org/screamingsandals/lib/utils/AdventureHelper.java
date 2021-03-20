package org.screamingsandals.lib.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class AdventureHelper {
    private final static LegacyComponentSerializer serializer;

    static {
        serializer = LegacyComponentSerializer.builder()
                .hexColors()
                .useUnusualXRepeatedCharacterHexFormat()
                .character(LegacyComponentSerializer.SECTION_CHAR)
                .build();
    }

    @NotNull
    public String toLegacy(@NotNull Component component) {
        return serializer.serialize(component);
    }

    @NotNull
    public String toLegacyNullable(@Nullable Component component) {
        return component == null ? "" : serializer.serialize(component);
    }

    @NotNull
    public TextComponent toComponent(@NotNull String input) {
        return serializer.deserialize(input);
    }

    @NotNull
    public TextComponent toComponentNullable(@Nullable String input) {
        return input == null ? Component.empty() : serializer.deserialize(input);
    }
}
