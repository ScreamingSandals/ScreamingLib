package org.screamingsandals.lib.lang;

import lombok.Data;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.TextComponent;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;

import java.util.List;

@Data
@ApiStatus.Internal
public class MessagePlaceholder implements Placeholder {
    private final String name;
    private final LangService service;
    private final CommandSenderWrapper sender;

    @SuppressWarnings("unchecked")
    @Override
    public <B extends Component.Builder<B, C>, C extends Component> @NotNull B getResult(MiniMessageParser parser, List<String> arguments, Placeholder... placeholders) {
        if (arguments.isEmpty()) {
            return (B) Component.text(); // invalid
        }
        var key = arguments.get(0).split("(?<!\\\\)\\.");
        Translation translation;
        if (arguments.size() > 1) {
            var fallback = arguments.get(1);
            translation = Translation.of(() -> Message.ofRichText(fallback).setRawPlaceholders(placeholders).asComponent(sender), key);
        } else {
            translation = Translation.of(key);
        }
        var msg = Message
                .of(service, translation)
                .setRawPlaceholders(placeholders)
                .asComponent(sender);
        if (msg instanceof TextComponent) {
            return (B) ((TextComponent) msg).toBuilder();
        }
        return (B) Component.text().append(msg);
    }
}
