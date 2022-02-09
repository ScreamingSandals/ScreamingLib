package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ClickEventSerializer implements TypeSerializer<ClickEvent> {
    public static final ClickEventSerializer INSTANCE = new ClickEventSerializer();

    private static final String ACTION_KEY = "action";
    private static final String VALUE_KEY = "value";

    @Override
    public ClickEvent deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            var action = ClickEvent.Action.valueOf(node.node(ACTION_KEY).getString("open_url").toUpperCase());
            var value = node.node(VALUE_KEY).getString("");
            return ClickEvent.builder()
                    .action(action)
                    .value(value)
                    .build();
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(Type type, @Nullable ClickEvent obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(ACTION_KEY).node(obj.action().name().toLowerCase());
        node.node(VALUE_KEY).node(obj.value());
    }
}
