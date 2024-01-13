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
import org.screamingsandals.lib.spectator.TranslatableComponent;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;

import java.util.ArrayList;

public class TranslatableFallbackResolver implements SingleTagResolver {

    @SuppressWarnings("unchecked")
    @Override
    public <B extends Component.Builder<B, C>, C extends Component> @Nullable B resolve(@NotNull MiniMessageParser parser, @NotNull TagNode tag, @NotNull Placeholder @NotNull... placeholders) {
        if (tag.getArgs().isEmpty()) {
            return null; // invalid
        }

        if (tag.getArgs().size() == 1) {
            return (B) Component.translatable().translate(tag.getArgs().get(0));
        }

        if (tag.getArgs().size() == 2) {
            return (B) Component.translatable().translate(tag.getArgs().get(0)).fallback(tag.getArgs().get(1));
        }

        TranslatableComponent.Builder builder = null;
        boolean fallback = false;
        var args = new ArrayList<Component>();

        for (var arg : tag.getArgs()) {
            if (builder == null) {
                builder = Component.translatable().translate(arg);
            } else if (!fallback) {
                fallback = true;
                builder.fallback(arg);
            } else {
                args.add(parser.parse(arg, placeholders));
            }
        }
        //noinspection ConstantConditions
        builder.args(args);

        return (B) builder;
    }

    @Override
    public @Nullable TagNode serialize(@NotNull MiniMessageParser parser, @NotNull String tagName, @NotNull Component component) {
        if (component instanceof TranslatableComponent) {
            var fallback = ((TranslatableComponent) component).fallback();
            if (fallback != null) {
                var args = new ArrayList<String>();
                args.add(((TranslatableComponent) component).translate());
                args.add(fallback);
                for (var arg : ((TranslatableComponent) component).args()) {
                    args.add(parser.serialize(arg));
                }
                return new TagNode(tagName, args);
            }
        }
        return null;
    }
}
