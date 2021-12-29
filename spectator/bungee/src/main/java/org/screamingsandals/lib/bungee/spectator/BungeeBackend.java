package org.screamingsandals.lib.bungee.spectator;

import net.md_5.bungee.api.chat.TextComponent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.SpectatorBackend;

public class BungeeBackend implements SpectatorBackend {
    @Override
    public Component empty() {
        return new BungeeComponent(new TextComponent());
    }
}
