package org.screamingsandals.lib.minestom.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.platform.minestom.MinestomComponentSerializer;
import net.kyori.adventure.text.Component;
import net.minestom.server.chat.JsonMessage;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class MinestomAdventureHelper {
    private final static MinestomComponentSerializer serializer;

    static {
        serializer = MinestomComponentSerializer.get();
    }

    public JsonMessage toMinestom(@NotNull Component component) {
        return serializer.serialize(component);
    }

    public Component toComponent(@NotNull JsonMessage input) {
        return serializer.deserialize(input);
    }
}
