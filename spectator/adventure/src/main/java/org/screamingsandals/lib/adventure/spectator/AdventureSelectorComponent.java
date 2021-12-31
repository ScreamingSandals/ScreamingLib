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

    public static class AdventureSelectorBuilder extends AdventureBuilder<
            net.kyori.adventure.text.SelectorComponent,
            SelectorComponent.Builder,
            SelectorComponent,
            net.kyori.adventure.text.SelectorComponent.Builder
            > implements SelectorComponent.Builder {

        public AdventureSelectorBuilder(net.kyori.adventure.text.SelectorComponent.Builder builder) {
            super(builder);
        }

        @Override
        public SelectorComponent.Builder pattern(String pattern) {
            getBuilder().pattern(pattern);
            return self();
        }

        @Override
        public SelectorComponent.Builder separator(@Nullable Component separator) {
            try {
                getBuilder().separator(separator == null ? null : separator.as(net.kyori.adventure.text.Component.class));
            } catch (Throwable ignored) {
                // added in Adventure 4.8.0
            }
            return self();
        }
    }
}
