package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.world.difficulty.DifficultyHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class DifficultyHolderSerializer implements TypeSerializer<DifficultyHolder> {
    public static final DifficultyHolderSerializer INSTANCE = new DifficultyHolderSerializer();

    @Override
    public DifficultyHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return DifficultyHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable DifficultyHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.platformName());
    }
}
