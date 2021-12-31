package org.screamingsandals.lib.adventure.spectator;

import net.kyori.adventure.text.NBTComponentBuilder;
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
/*
    public static class AdventureNBTBuilder extends AdventureBuilder<
            net.kyori.adventure.text.BuildableComponent<?,?>,
            NBTComponent.Builder,
            NBTComponent,
            NBTComponentBuilder
            > implements NBTComponent.Builder {

        public AdventureNBTBuilder(NBTComponentBuilder builder) {
            super(builder);
        }

        @Override
        public NBTComponent.Builder nbtPath(String nbtPath) {
            getBuilder().nbtPath(nbtPath);
            return self();
        }

        @Override
        public NBTComponent.Builder interpret(boolean interpret) {
            getBuilder().interpret(interpret);
            return self();
        }

        @Override
        public NBTComponent.Builder target(Target target) {

            return self();
        }

        @Override
        public NBTComponent.Builder separator(@Nullable Component separator) {
            try {
                getBuilder().separator(separator == null ? null : separator.as(net.kyori.adventure.text.Component.class));
            } catch (Throwable ignored) {
                // added in Adventure 4.8.0
            }
            return self();
        }
    }*/
}
