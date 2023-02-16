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

package org.screamingsandals.lib.spectator.mini.resolvers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.minitag.MiniTagParser;
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;

import java.util.List;
import java.util.Locale;

public class ClickResolver implements StylingResolver {
    @Override
    public <B extends Component.Builder<B, C>, C extends Component> void resolve(@NotNull MiniMessageParser parser, @NotNull B builder, @NotNull TagNode tag, @NotNull Placeholder @NotNull... placeholders) {
        if (tag.getArgs().size() < 2) {
            return;
        }

        builder.clickEvent(ClickEvent.builder()
                .action(ClickEvent.Action.valueOf(tag.getArgs().get(0).toUpperCase(Locale.ROOT)))
                .value(
                        parser.resolvePlaceholdersInString(tag.getArgs().get(1), parser.extractStringLikePlaceholders(placeholders))
                )
                .build()
        );
    }

    @Override
    public @Nullable TagNode serialize(@NotNull MiniMessageParser parser, @NotNull String tagName, @NotNull Component component) {
        var click = component.clickEvent();
        if (click != null) {
            return new TagNode(tagName, List.of(click.action().name().toLowerCase(Locale.ROOT), click.value()));
        }
        return null;
    }
}
