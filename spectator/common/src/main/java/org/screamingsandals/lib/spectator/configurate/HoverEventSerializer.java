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

package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.Content;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.Preconditions;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Locale;

public class HoverEventSerializer implements TypeSerializer<HoverEvent> {
    public static final @NotNull HoverEventSerializer INSTANCE = new HoverEventSerializer();

    private static final @NotNull String ACTION_KEY = "action";
    private static final @NotNull String CONTENTS_KEY = "contents";
    private static final @NotNull String VALUE_KEY = "value";

    @Override
    public @NotNull HoverEvent deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            var action = HoverEvent.Action.valueOf(node.node(ACTION_KEY).getString("show_text").toUpperCase(Locale.ROOT));
            Content content;
            if (node.hasChild(VALUE_KEY)) {
                var value = node.node(VALUE_KEY);
                switch (action) {
                    case SHOW_ITEM:
                        // TODO
                    case SHOW_ENTITY:
                        // TODO
                    default:
                        content = value.get(Component.class);
                }
            } else {
                var contents = node.node(CONTENTS_KEY);
                switch (action) {
                    case SHOW_ITEM:
                        content = contents.get(ItemContent.class);
                        break;
                    case SHOW_ENTITY:
                        content = contents.get(EntityContent.class);
                        break;
                    default:
                        content = contents.get(Component.class);
                }
            }
            Preconditions.checkNotNull(content);
            return HoverEvent.builder()
                    .action(action)
                    .content(content)
                    .build();
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable HoverEvent obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(ACTION_KEY).set(obj.action().name().toLowerCase(Locale.ROOT)); // lower case to match vanilla keys
        switch (obj.action()) {
            case SHOW_ITEM:
                node.node(CONTENTS_KEY).set(ItemContent.class, (ItemContent) obj.content());
                break;
            case SHOW_ENTITY:
                node.node(CONTENTS_KEY).set(EntityContent.class, (EntityContent) obj.content());
                break;
            default:
                node.node(CONTENTS_KEY).set(Component.class, (Component) obj.content());
        }
    }
}
