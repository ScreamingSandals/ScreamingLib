package org.screamingsandals.lib.core.lang.storage;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@ConfigSerializable
public class MessageContainer {
    private Map<String, String> placeholders = new HashMap<>();

    private String text = "";

    public MessageContainer(String text) {
        this.text = text;
    }

    public Component toComponent(Map<String, String> placeholders) {
        this.placeholders = placeholders;
        return toComponent();
    }

    public Component toComponent() {
        return MiniMessage.get().parse(text, placeholders);
    }

    public MessageContainer addPlaceholders(Map<String, String> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }
}
