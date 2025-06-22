/*
 * Copyright 2025 ScreamingSandals
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
import org.screamingsandals.lib.nbt.Tag;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.click.Payload;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.List;
import java.util.Locale;

public class ClickResolver implements StylingResolver {
    private static final @NotNull SNBTSerializer internalSNBTSerializer = SNBTSerializer.builder().shouldSaveLongArraysDirectly(true).build();

    @Override
    public <B extends Component.Builder<B, C>, C extends Component> void applyStyle(@NotNull MiniMessageParser parser, @NotNull B builder, @NotNull TagNode tag, @NotNull Placeholder @NotNull... placeholders) {
        if (tag.getArgs().size() < 2) {
            return;
        }

        var action = ClickEvent.Action.valueOf(tag.getArgs().get(0).toUpperCase(Locale.ROOT));

        if (action == ClickEvent.Action.CUSTOM) {
            var id = ResourceLocation.of(tag.getArgs().get(1));
            Tag payload;
            if (tag.getArgs().size() > 2) {
                payload = internalSNBTSerializer.deserialize(tag.getArgs().get(2));
            } else {
                payload = CompoundTag.EMPTY;
            }
            builder.clickEvent(ClickEvent.custom(id, payload));
        } else if (action == ClickEvent.Action.SHOW_DIALOG) {
            // TODO: dialog deserializing (needs support in Adventure)
        } else {
            builder.clickEvent(ClickEvent.builder()
                    .action(action)
                    .payload(Payload.text(
                            parser.resolvePlaceholdersInString(tag.getArgs().get(1), placeholders)
                    ))
                    .build()
            );
        }
    }

    @Override
    public @Nullable TagNode serialize(@NotNull MiniMessageParser parser, @NotNull String tagName, @NotNull Component component) {
        var click = component.clickEvent();
        if (click != null) {
            if (click.action() == ClickEvent.Action.CUSTOM) {
                var payload = click.payload();
                if (!(payload instanceof Payload.Custom)) {
                    return null; // Should not happen
                }
                return new TagNode(tagName, List.of(
                        click.action().name().toLowerCase(Locale.ROOT),
                        ((Payload.Custom) payload).location().toString(),
                        internalSNBTSerializer.serialize(((Payload.Custom) payload).tag())
                ));
            } else if (click.action() == ClickEvent.Action.SHOW_DIALOG) {
                // TODO: dialog serializing (needs support in Adventure)
            } else {
                String value;
                if (click.payload() instanceof Payload.Text) {
                    value = ((Payload.Text) click.payload()).text();
                } else if (click.payload() instanceof Payload.Int) {
                    value = String.valueOf(((Payload.Int) click.payload()).number());
                } else {
                    return null; // Don't know how to serialize
                }
                return new TagNode(tagName, List.of(click.action().name().toLowerCase(Locale.ROOT), value));
            }
        }
        return null;
    }
}
