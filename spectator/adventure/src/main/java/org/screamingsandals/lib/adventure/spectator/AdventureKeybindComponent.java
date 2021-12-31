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

    public static class AdventureKeybindBuilder extends AdventureBuilder<
            net.kyori.adventure.text.KeybindComponent,
            KeybindComponent.Builder,
            KeybindComponent,
            net.kyori.adventure.text.KeybindComponent.Builder
            > implements KeybindComponent.Builder {

        public AdventureKeybindBuilder(net.kyori.adventure.text.KeybindComponent.Builder builder) {
            super(builder);
        }

        @Override
        public KeybindComponent.Builder keybind(String keybind) {
            getBuilder().keybind(keybind);
            return self();
        }
    }
}
