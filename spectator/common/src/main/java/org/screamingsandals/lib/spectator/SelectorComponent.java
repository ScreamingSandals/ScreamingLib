package org.screamingsandals.lib.spectator;

public interface SelectorComponent extends SeparableComponent {
    String pattern();

    interface Builder extends SeparableComponent.Builder<Builder, SelectorComponent> {
        Builder pattern(String pattern);
    }
}
