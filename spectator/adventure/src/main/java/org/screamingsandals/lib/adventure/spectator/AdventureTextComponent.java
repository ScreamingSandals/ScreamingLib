package org.screamingsandals.lib.adventure.spectator;

import org.screamingsandals.lib.spectator.TextComponent;

public class AdventureTextComponent extends AdventureComponent implements TextComponent {
    public AdventureTextComponent(net.kyori.adventure.text.TextComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String content() {
        return ((TextComponent) wrappedObject).content();
    }
}
