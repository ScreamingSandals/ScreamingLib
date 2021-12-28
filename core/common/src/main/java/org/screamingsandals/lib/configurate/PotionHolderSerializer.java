package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.item.meta.PotionHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class PotionHolderSerializer implements TypeSerializer<PotionHolder> {
    @Override
    public PotionHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return PotionHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable PotionHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.platformName());
    }
}
