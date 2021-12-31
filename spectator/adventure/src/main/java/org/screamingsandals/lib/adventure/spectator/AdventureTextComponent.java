package org.screamingsandals.lib.adventure.spectator;

import org.screamingsandals.lib.spectator.TextComponent;

public class AdventureTextComponent extends AdventureComponent implements TextComponent {
    public AdventureTextComponent(net.kyori.adventure.text.TextComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String content() {
        return ((TextComponent) wrappedObject).content();
    }

    public static class AdventureTextBuilder extends AdventureBuilder<
            net.kyori.adventure.text.TextComponent,
            TextComponent.Builder,
            TextComponent,
            net.kyori.adventure.text.TextComponent.Builder
            > implements TextComponent.Builder {

        public AdventureTextBuilder(net.kyori.adventure.text.TextComponent.Builder builder) {
            super(builder);
        }

        @Override
        public TextComponent.Builder content(String content) {
            getBuilder().content(content);
            return self();
        }
    }
}
