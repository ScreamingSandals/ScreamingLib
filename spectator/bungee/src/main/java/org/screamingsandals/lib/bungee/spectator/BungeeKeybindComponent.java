package org.screamingsandals.lib.bungee.spectator;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.KeybindComponent;

public class BungeeKeybindComponent extends BungeeComponent implements KeybindComponent {
    protected BungeeKeybindComponent(net.md_5.bungee.api.chat.KeybindComponent wrappedObject) {
        super(wrappedObject);
    }

    @NotNull
    public String keybind() {
        return ((net.md_5.bungee.api.chat.KeybindComponent) wrappedObject).getKeybind();
    }

    @Override
    @NotNull
    public KeybindComponent withKeybind(@NotNull String keybind) {
        var duplicate = (net.md_5.bungee.api.chat.KeybindComponent) wrappedObject.duplicate();
        duplicate.setKeybind(keybind);
        return (KeybindComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @NotNull
    @Override
    public KeybindComponent.Builder toBuilder() {
        var duplicate = (net.md_5.bungee.api.chat.KeybindComponent) wrappedObject.duplicate();
        return new BungeeKeybindBuilder(duplicate);
    }

    public static class BungeeKeybindBuilder extends BungeeBuilder<KeybindComponent, KeybindComponent.Builder, net.md_5.bungee.api.chat.KeybindComponent> implements KeybindComponent.Builder {

        public BungeeKeybindBuilder(net.md_5.bungee.api.chat.KeybindComponent component) {
            super(component);
        }

        @Override
        @NotNull
        public KeybindComponent.Builder keybind(@NotNull String keybind) {
            component.setKeybind(keybind);
            return this;
        }
    }
}
