package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ComponentSerializer implements TypeSerializer<Component> {
    public static final ComponentSerializer INSTANCE = new ComponentSerializer();

    @Override
    public Component deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            // TODO

            return null;
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(Type type, @Nullable Component obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        // TODO
    }
}
