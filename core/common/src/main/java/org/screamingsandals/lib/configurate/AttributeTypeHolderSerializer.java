package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class AttributeTypeHolderSerializer implements TypeSerializer<AttributeTypeHolder> {
    public static final AttributeTypeHolderSerializer INSTANCE = new AttributeTypeHolderSerializer();

    @Override
    public AttributeTypeHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return AttributeTypeHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable AttributeTypeHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.platformName());
    }
}
