package org.screamingsandals.lib.adventure.spectator;

import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.SpectatorBackend;

public class AdventureBackend implements SpectatorBackend {
    @Override
    public Component empty() {
        return new AdventureComponent(net.kyori.adventure.text.Component.empty());
    }
}
