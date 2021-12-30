package org.screamingsandals.lib.adventure.spectator;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.SelectorComponent;

public class AdventureSelectorComponent extends AdventureComponent implements SelectorComponent {
    public AdventureSelectorComponent(net.kyori.adventure.text.SelectorComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String pattern() {
        return ((net.kyori.adventure.text.SelectorComponent) wrappedObject).pattern();
    }

    @Override
    @Nullable
    public Component separator() {
        try {
            return AdventureBackend.wrapComponent(((net.kyori.adventure.text.SelectorComponent) wrappedObject).separator());
        } catch (Throwable ignored) {
            return null; // added in Adventure 4.8.0
        }
    }
}
