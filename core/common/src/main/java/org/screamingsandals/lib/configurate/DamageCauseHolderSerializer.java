package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class DamageCauseHolderSerializer implements TypeSerializer<DamageCauseHolder> {
    public static final DamageCauseHolderSerializer INSTANCE = new DamageCauseHolderSerializer();

    @Override
    public DamageCauseHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return DamageCauseHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable DamageCauseHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.platformName());
    }
}
