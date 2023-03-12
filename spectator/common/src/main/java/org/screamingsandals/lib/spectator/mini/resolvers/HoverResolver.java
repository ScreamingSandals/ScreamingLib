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
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.nbt.SNBTSerializer;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;
import org.screamingsandals.lib.utils.key.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class HoverResolver implements StylingResolver {
    private static final @NotNull SNBTSerializer internalSNBTSerializer = SNBTSerializer.builder().shouldSaveLongArraysDirectly(true).build();

    @Override
    public <B extends Component.Builder<B, C>, C extends Component> void applyStyle(@NotNull MiniMessageParser parser, @NotNull B builder, @NotNull TagNode tag, @NotNull Placeholder @NotNull... placeholders) {
        if (tag.getArgs().isEmpty()) {
            return;
        }

        if (tag.getArgs().size() == 1) {
            builder.hoverEvent(parser.parse(tag.getArgs().get(0), placeholders));
            return;
        }

        switch (tag.getArgs().get(0).toLowerCase(Locale.ROOT)) {
            case "show_item":
            case "item":
                var item = ItemContent.builder();
                item.id(ResourceLocation.of(tag.getArgs().get(1)));
                if (tag.getArgs().size() > 2) {
                    try {
                        var count = Integer.parseInt(tag.getArgs().get(2));
                        item.count(count);
                    } catch (NumberFormatException ignored) {}
                    if (tag.getArgs().size() > 3) {
                        var snbt = tag.getArgs().get(3);
                        item.tag((CompoundTag) internalSNBTSerializer.deserialize(snbt));
                    }
                }
                builder.hoverEvent(item);
                break;
            case "show_entity":
            case "entity":
                if (tag.getArgs().size() > 2) {
                    var entity = EntityContent.builder();
                    entity.type(ResourceLocation.of(tag.getArgs().get(1)));
                    entity.id(UUID.fromString(tag.getArgs().get(2)));
                    if (tag.getArgs().size() > 3) {
                        entity.name(parser.parse(tag.getArgs().get(3), placeholders));
                    }
                    builder.hoverEvent(entity);
                }
                break;
            default:
                builder.hoverEvent(parser.parse(tag.getArgs().get(1), placeholders));
        }

    }

    @Override
    public @Nullable TagNode serialize(@NotNull MiniMessageParser parser, @NotNull String tagName, @NotNull Component component) {
        var hover = component.hoverEvent();
        if (hover != null) {
            switch (hover.action()) {
                case SHOW_ITEM: {
                    var args = new ArrayList<String>();
                    args.add("show_item");
                    var item = (ItemContent) hover.content();
                    args.add(item.id().asString());
                    var count = item.count();
                    var tag = item.tag();
                    if (count != 1 || tag != null) {
                        args.add(String.valueOf(count));
                        if (tag != null) {
                            args.add(internalSNBTSerializer.serialize(tag));
                        }
                    }
                    return new TagNode(tagName, args);
                }
                case SHOW_ENTITY: {
                    var args = new ArrayList<String>();
                    args.add("show_entity");
                    var entity = (EntityContent) hover.content();
                    args.add(entity.type().asString());
                    args.add(entity.id().toString());
                    var name = entity.name();
                    if (name != null) {
                        args.add(parser.serialize(name));
                    }
                    return new TagNode(tagName, args);
                }
                default:
                    return new TagNode(tagName, List.of("show_text", parser.serialize((Component) hover.content())));
            }
        }
        return null;
    }
}
