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
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.UUID;

public class HoverResolver implements StylingResolver {

    @Override
    public <B extends Component.Builder<B, C>, C extends Component> void resolve(@NotNull MiniMessageParser parser, @NotNull B builder, @NotNull TagNode tag) {
        if (tag.getArgs().isEmpty()) {
            return;
        }

        if (tag.getArgs().size() == 1) {
            builder.hoverEvent(parser.parse(tag.getArgs().get(0))); // TODO: Preserve placeholders
            return;
        }

        switch (tag.getArgs().get(0).toLowerCase()) {
            case "show_item":
            case "item":
                var item = ItemContent.builder();
                item.id(NamespacedMappingKey.of(tag.getArgs().get(1)));
                if (tag.getArgs().size() > 2) {
                    try {
                        var count = Integer.parseInt(tag.getArgs().get(2));
                        item.count(count);
                    } catch (NumberFormatException ignored) {}
                    if (tag.getArgs().size() > 3) {
                        var snbt = tag.getArgs().get(3);
                        item.tag(snbt);
                    }
                }
                builder.hoverEvent(item);
                break;
            case "show_entity":
            case "entity":
                if (tag.getArgs().size() > 2) {
                    var entity = EntityContent.builder();
                    entity.type(NamespacedMappingKey.of(tag.getArgs().get(1)));
                    entity.id(UUID.fromString(tag.getArgs().get(2)));
                    if (tag.getArgs().size() > 3) {
                        entity.name(parser.parse(tag.getArgs().get(0))); // TODO: Preserve placeholders
                    }
                    builder.hoverEvent(entity);
                }
                break;
            default:
                builder.hoverEvent(parser.parse(tag.getArgs().get(0))); // TODO: Preserve placeholders
        }

    }
}
