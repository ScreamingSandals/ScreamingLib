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
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ScoreComponent;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;

import java.util.ArrayList;

public class ScoreResolver implements ComponentBuilderResolver {

    // Exclusive: <score:name:objective[:value]>
    @SuppressWarnings("unchecked")
    @Override
    public <B extends Component.Builder<B, C>, C extends Component> @Nullable B resolve(@NotNull MiniMessageParser parser, @NotNull TagNode tag, @NotNull Placeholder @NotNull... placeholders) {
        if (tag.getArgs().size() < 2) {
            return null; // invalid
        }

        if (tag.getArgs().size() == 2) {
            return (B) Component.score().name(tag.getArgs().get(0)).objective(tag.getArgs().get(1));
        }

        return (B) Component.score()
                .name(tag.getArgs().get(0))
                .objective(tag.getArgs().get(1))
                .value(tag.getArgs().get(2));
    }

    @Override
    public @Nullable TagNode serialize(@NotNull MiniMessageParser parser, @NotNull String tagName, @NotNull Component component) {
        if (component instanceof ScoreComponent) {
            var args = new ArrayList<String>();
            args.add(((ScoreComponent) component).name());
            args.add(((ScoreComponent) component).objective());
            if (((ScoreComponent) component).value() != null) {
                args.add(((ScoreComponent) component).value());
            }
            return new TagNode(tagName, args);
        }
        return null;
    }
}
