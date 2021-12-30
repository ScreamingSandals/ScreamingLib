package org.screamingsandals.lib.adventure.spectator;

import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.TranslatableComponent;

import java.util.List;
import java.util.stream.Collectors;

public class AdventureTranslatableComponent extends AdventureComponent implements TranslatableComponent {
    public AdventureTranslatableComponent(net.kyori.adventure.text.TranslatableComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String translate() {
        return ((net.kyori.adventure.text.TranslatableComponent) wrappedObject).key();
    }

    @Override
    public List<Component> args() {
        return ((net.kyori.adventure.text.TranslatableComponent) wrappedObject).args()
                .stream()
                .map(AdventureBackend::wrapComponent)
                .collect(Collectors.toList());
    }
}
