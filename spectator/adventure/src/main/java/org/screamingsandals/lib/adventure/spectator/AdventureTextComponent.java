package org.screamingsandals.lib.adventure.spectator;

import org.screamingsandals.lib.spectator.TextComponent;

public class AdventureTextComponent extends AdventureComponent implements TextComponent {
    protected AdventureTextComponent(net.kyori.adventure.text.TextComponent wrappedObject) {
        super(wrappedObject);
    }
}
