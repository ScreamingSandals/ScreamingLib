package org.screamingsandals.lib.bungee.spectator;

import org.screamingsandals.lib.spectator.TextComponent;

public class BungeeTextComponent extends BungeeComponent implements TextComponent {
    protected BungeeTextComponent(net.md_5.bungee.api.chat.TextComponent wrappedObject) {
        super(wrappedObject);
    }
}
