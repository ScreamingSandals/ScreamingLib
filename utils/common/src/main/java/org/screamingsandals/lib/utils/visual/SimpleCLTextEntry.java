package org.screamingsandals.lib.utils.visual;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;

@AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
@Data
public class SimpleCLTextEntry implements TextEntry {
    private final String identifier;
    private final ComponentLike text;

    static SimpleCLTextEntry of(ComponentLike text) {
        return SimpleCLTextEntry.of("", text);
    }

    public Component getText() {
        return text.asComponent();
    }

    public ComponentLike getComponentLike() {
        return text;
    }
}
