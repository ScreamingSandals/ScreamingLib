/*
 * Copyright 2022 ScreamingSandals
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
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.TranslatableComponent;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;

import java.util.ArrayList;

public class TranslatableResolver implements ComponentBuilderResolver {

    @SuppressWarnings("unchecked")
    @Override
    public <B extends Component.Builder<B, C>, C extends Component> B resolve(@NotNull MiniMessageParser parser, @NotNull TagNode tag) {
        if (tag.getArgs().isEmpty()) {
            return null; // invalid
        }

        if (tag.getArgs().size() == 1) {
            return (B) Component.translatable().translate(tag.getArgs().get(0));
        }

        TranslatableComponent.Builder builder = null;
        var args = new ArrayList<Component>();

        for (var arg : tag.getArgs()) {
            if (builder == null) {
                builder = Component.translatable().translate(arg);
            } else {
                args.add(parser.parse(arg)); // TODO: Preserve placeholders
            }
        }
        //noinspection ConstantConditions
        builder.args(args);

        return (B) builder;
    }
}
