package org.screamingsandals.lib.adventure.spectator;

import org.screamingsandals.lib.spectator.KeybindComponent;

public class AdventureKeybindComponent extends AdventureComponent implements KeybindComponent {
    public AdventureKeybindComponent(net.kyori.adventure.text.KeybindComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String keybind() {
        return ((net.kyori.adventure.text.KeybindComponent) wrappedObject).keybind();
    }
}
