package org.screamingsandals.lib.adventure.spectator;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.NBTComponent;

public abstract class AdventureNBTComponent<C extends net.kyori.adventure.text.NBTComponent<C,?>> extends AdventureComponent implements NBTComponent {
    public AdventureNBTComponent(C wrappedObject) {
        super(wrappedObject);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String nbtPath() {
        return ((C) wrappedObject).nbtPath();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean interpret() {
        return ((C) wrappedObject).interpret();
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
