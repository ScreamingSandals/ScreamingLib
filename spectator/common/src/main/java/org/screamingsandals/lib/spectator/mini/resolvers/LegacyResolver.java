package org.screamingsandals.lib.spectator.mini.resolvers;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.TextComponent;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;

public class LegacyResolver implements ComponentBuilderResolver {
    @SuppressWarnings("unchecked")
    @Override
    public <B extends Component.Builder<B, C>, C extends Component> B resolve(@NotNull MiniMessageParser parser, @NotNull TagNode tag, Placeholder... placeholders) {
        if (tag.getArgs().isEmpty()) {
            return (B) Component.text();
        }

        // legacy text should be always text component
        return (B) ((TextComponent) Component.fromLegacy(tag.getArgs().get(0))).toBuilder();
    }
}
