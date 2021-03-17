package org.screamingsandals.lib.utils.visual;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;

@AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
@Data
class SimpleTextEntry implements TextEntry {
    private final String identifier;
    private final Component text;

    static SimpleTextEntry of(Component text) {
        return SimpleTextEntry.of("", text);
    }
}
