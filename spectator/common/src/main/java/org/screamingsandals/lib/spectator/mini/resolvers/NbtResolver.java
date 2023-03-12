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
import org.screamingsandals.lib.spectator.*;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;
import org.screamingsandals.lib.utils.key.ResourceLocation;

import java.util.ArrayList;
import java.util.Locale;

public class NbtResolver implements SingleTagResolver {

    @SuppressWarnings("unchecked")
    @Override
    public <B extends Component.Builder<B, C>, C extends Component> @Nullable B resolve(@NotNull MiniMessageParser parser, @NotNull TagNode tag, @NotNull Placeholder @NotNull... placeholders) {
        if (tag.getArgs().size() < 3) {
            return null; // invalid
        }

        var name = tag.getArgs().get(0).toLowerCase(Locale.ROOT);
        var secondArgument = tag.getArgs().get(1);
        var nbtPathArgument = tag.getArgs().get(2);

        NBTComponent.Builder<?, ?> builder;

        switch (name) {
            case "block":
                builder = Component.blockNBT().blockPosition(secondArgument);
                break;
            case "entity":
                builder = Component.entityNBT().selector(secondArgument);
                break;
            case "storage":
                builder = Component.storageNBT().storageKey(ResourceLocation.of(secondArgument));
                break;
            default:
                return null; // invalid
        }

        builder.nbtPath(nbtPathArgument);

        if (tag.getArgs().size() >= 4) {
            var thirdValue = tag.getArgs().get(3);

            if ("interpret".equalsIgnoreCase(thirdValue)) {
                builder.interpret(true);
            } else {
                builder.separator(parser.parse(thirdValue, placeholders));

                if (tag.getArgs().size() >= 5 && "interpret".equalsIgnoreCase(tag.getArgs().get(4))) {
                    builder.interpret(true);
                }
            }
        }

        return (B) builder;
    }

    @Override
    public @Nullable TagNode serialize(@NotNull MiniMessageParser parser, @NotNull String tagName, @NotNull Component component) {
        if (component instanceof NBTComponent) {
            var args = new ArrayList<String>();
            if (component instanceof BlockNBTComponent) {
                args.add("block");
                args.add(((BlockNBTComponent) component).blockPosition());
            } else if (component instanceof EntityNBTComponent) {
                args.add("entity");
                args.add(((EntityNBTComponent) component).selector());
            } else if (component instanceof StorageNBTComponent) {
                args.add("storage");
                args.add(((StorageNBTComponent) component).storageKey().asString());
            } else {
                return null; // unknown nbt component
            }
            args.add(((NBTComponent) component).nbtPath());

            var separator = ((NBTComponent) component).separator();
            if (separator != null) {
                args.add(parser.serialize(separator));
            }

            if (((NBTComponent) component).interpret()) {
                args.add("interpret");
            }

            return new TagNode(tagName, args);
        }
        return null;
    }
}
