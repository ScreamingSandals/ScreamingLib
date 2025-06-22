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

package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.nbt.SNBTSerializer;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.click.Payload;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Objects;

public class ClickEventSerializer implements TypeSerializer<ClickEvent> {
    private static final @NotNull SNBTSerializer internalSNBTSerializer = SNBTSerializer.builder().shouldSaveLongArraysDirectly(true).build();
    public static final @NotNull ClickEventSerializer INSTANCE = new ClickEventSerializer();

    private static final @NotNull String ACTION_KEY = "action";
    private static final @NotNull String VALUE_KEY = "value";

    private static final @NotNull String URL_KEY = "url";
    private static final @NotNull String COMMAND_KEY = "command";
    private static final @NotNull String PAGE_KEY = "page";

    private static final @NotNull String DIALOG_KEY = "dialog";
    private static final @NotNull String ID_KEY = "id";
    private static final @NotNull String PAYLOAD_KEY = "payload";

    @Override
    public @NotNull ClickEvent deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            var action = ClickEvent.Action.valueOf(node.node(ACTION_KEY).getString("open_url").toUpperCase(Locale.ROOT));
            if (action == ClickEvent.Action.CUSTOM) {
                var id = ResourceLocation.of(Objects.requireNonNull(node.node(ID_KEY).getString()));
                var payload = node.node(PAYLOAD_KEY).getString();
                var payloadTag = payload != null ? internalSNBTSerializer.deserialize(payload) : CompoundTag.EMPTY;

                return ClickEvent.custom(id, payloadTag);
            } else if (action == ClickEvent.Action.SHOW_DIALOG) {
                // TODO: dialog deserializing (needs support in Adventure)
                throw new UnsupportedOperationException("Not implemented yet");
                // return ClickEvent.showDialog(dialog);
            } else {
                String value;
                if (action == ClickEvent.Action.OPEN_URL && node.hasChild(URL_KEY)) {
                    value = node.node(URL_KEY).getString("");
                } else if ((action == ClickEvent.Action.RUN_COMMAND || action == ClickEvent.Action.SUGGEST_COMMAND) && node.hasChild(COMMAND_KEY)) {
                    value = node.node(COMMAND_KEY).getString("");
                } else if (action == ClickEvent.Action.CHANGE_PAGE && node.hasChild(PAGE_KEY)) {
                    value = node.node(PAGE_KEY).getString("");
                } else {
                    value = node.node(VALUE_KEY).getString("");
                }
                return ClickEvent.builder()
                        .action(action)
                        .payload(Payload.text(value))
                        .build();
            }
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable ClickEvent obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(ACTION_KEY).set(obj.action().name().toLowerCase(Locale.ROOT));
        var payload = obj.payload();
        if (payload instanceof Payload.Custom) {
            node.node(ID_KEY).set(((Payload.Custom) payload).location().toString());
            node.node(PAYLOAD_KEY).set(internalSNBTSerializer.serialize(((Payload.Custom) payload).tag()));
        } else if (payload instanceof Payload.ShowDialog) {
            // TODO: dialog serializing (needs support in Adventure)
            throw new SerializationException(new UnsupportedOperationException("Not implemented yet"));
        } else if (payload instanceof Payload.Int) {
            node.node(PAGE_KEY).set(((Payload.Int) payload).number());
        } else if (payload instanceof Payload.Text) {
            var value = ((Payload.Text) payload).text();
            switch (obj.action()) {
                case OPEN_URL:
                    node.node(URL_KEY).set(value);
                    break;
                case RUN_COMMAND:
                case SUGGEST_COMMAND:
                    node.node(COMMAND_KEY).set(value);
                    break;
                case CHANGE_PAGE:
                    node.node(PAGE_KEY).set(value);
                    break;
                default:
                    node.node(VALUE_KEY).set(value);
            }
        } else {
            throw new SerializationException(new UnsupportedOperationException("Unknown payload of type " + payload.getClass()));
        }
    }
}
