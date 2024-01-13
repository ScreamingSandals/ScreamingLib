/*
 * Copyright 2024 ScreamingSandals
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
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;
import org.screamingsandals.lib.utils.TriState;

import java.util.List;

public class BoldResolver implements StylingResolver {
    @Override
    public <B extends Component.Builder<B, C>, C extends Component> void applyStyle(@NotNull MiniMessageParser parser, @NotNull B builder, @NotNull TagNode tag, @NotNull Placeholder @NotNull... placeholders) {
        if (tag.getArgs().isEmpty()) {
            builder.bold();
        } else {
            builder.bold(Boolean.parseBoolean(tag.getArgs().get(0)));
        }
    }

    @Override
    public @Nullable TagNode serialize(@NotNull MiniMessageParser parser, @NotNull String tagName, @NotNull Component component) {
        if (component.bold() == TriState.TRUE) {
            return new TagNode(tagName, List.of());
        } else if (component.bold() == TriState.FALSE) {
            return new TagNode(tagName, List.of("false"));
        }
        return null;
    }
}
