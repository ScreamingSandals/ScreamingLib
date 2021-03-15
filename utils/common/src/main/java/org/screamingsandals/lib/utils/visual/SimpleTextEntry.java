package org.screamingsandals.lib.utils.visual;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;

@AllArgsConstructor(staticName = "of")
@Data
public class SimpleTextEntry implements TextEntry {
    private final String identifier;
    private final Component text;

    public static SimpleTextEntry of(Component text) {
        return SimpleTextEntry.of("", text);
    }
}
