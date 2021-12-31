package org.screamingsandals.lib.bungee.spectator;

import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.TranslatableComponent;

import java.util.List;
import java.util.stream.Collectors;

public class BungeeTranslatableContent extends BungeeComponent implements TranslatableComponent {
    protected BungeeTranslatableContent(net.md_5.bungee.api.chat.TranslatableComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String translate() {
        return ((net.md_5.bungee.api.chat.TranslatableComponent) wrappedObject).getTranslate();
    }

    @Override
    public List<Component> args() {
        var with = ((net.md_5.bungee.api.chat.TranslatableComponent) wrappedObject).getWith();
        if (with == null || with.isEmpty()) {
            return List.of();
        }
        return with.stream()
                .map(BungeeBackend::wrapComponent)
                .collect(Collectors.toList());
    }
}
