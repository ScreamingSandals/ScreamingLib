package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.world.dimension.DimensionHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class DimensionHolderSerializer extends AbstractScreamingSerializer implements TypeSerializer<DimensionHolder> {
    public static final DimensionHolderSerializer INSTANCE = new DimensionHolderSerializer();

    @Override
    public DimensionHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return DimensionHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable DimensionHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.platformName());
    }
}
