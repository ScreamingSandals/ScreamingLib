package org.screamingsandals.lib.bungee.spectator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.SelectorComponent;

public class BungeeSelectorComponent extends BungeeComponent implements SelectorComponent {
    protected BungeeSelectorComponent(net.md_5.bungee.api.chat.SelectorComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public String pattern() {
        return ((net.md_5.bungee.api.chat.SelectorComponent) wrappedObject).getSelector();
    }

    @Override
    @NotNull
    public SelectorComponent withPattern(String pattern) {
        var duplicate = (net.md_5.bungee.api.chat.SelectorComponent) wrappedObject.duplicate();
        duplicate.setSelector(pattern);
        return (SelectorComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    @NotNull
    public SelectorComponent.Builder toBuilder() {
        var duplicate = (net.md_5.bungee.api.chat.SelectorComponent) wrappedObject.duplicate();
        return new BungeeSelectorBuilder(duplicate);
    }

    @Override
    @Nullable
    public Component separator() {
        return null; // TODO: WHERE IS IT ???
    }

    @Override
    @NotNull
    public SelectorComponent withSeparator(@Nullable Component separator) {
        return this; // TODO: WHERE IS IT ???
    }

    public static class BungeeSelectorBuilder extends BungeeBuilder<SelectorComponent, SelectorComponent.Builder, net.md_5.bungee.api.chat.SelectorComponent> implements SelectorComponent.Builder {

        public BungeeSelectorBuilder(net.md_5.bungee.api.chat.SelectorComponent component) {
            super(component);
        }

        @Override
        @NotNull
        public SelectorComponent.Builder pattern(@NotNull String pattern) {
            component.setSelector(pattern);
            return this;
        }

        @Override
        @NotNull
        public SelectorComponent.Builder separator(@Nullable Component separator) {
            // TODO: Hey md_5, I hate you with all my hearth
            return this;
        }
    }
}
