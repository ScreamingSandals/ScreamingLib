/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    public <B extends Component.Builder<B, C>, C extends Component> @NotNull B getResult(@NotNull MiniMessageParser parser, @NotNull List<@NotNull String> arguments, @NotNull Placeholder @NotNull ... placeholders) {
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
