package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.entity.pose.EntityPoseHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class EntityPoseHolderSerializer implements TypeSerializer<EntityPoseHolder> {
    @Override
    public EntityPoseHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return EntityPoseHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable EntityPoseHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.platformName());
    }
}
