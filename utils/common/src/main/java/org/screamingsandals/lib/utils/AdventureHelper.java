package org.screamingsandals.lib.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

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

    public String toLegacy(@NotNull Component component) {
        return serializer.serialize(component);
    }

    public TextComponent toComponent(@NotNull String input) {
        return serializer.deserialize(input);
    }
}
