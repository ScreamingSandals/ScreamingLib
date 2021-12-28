package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class EntityTypeHolderSerializer extends AbstractScreamingSerializer implements TypeSerializer<EntityTypeHolder> {
    public static final EntityTypeHolderSerializer INSTANCE = new EntityTypeHolderSerializer();

    @Override
    public EntityTypeHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return EntityTypeHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable EntityTypeHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.platformName());
    }
}