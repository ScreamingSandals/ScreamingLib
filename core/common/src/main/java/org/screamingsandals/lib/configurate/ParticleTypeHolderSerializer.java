package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.particle.ParticleTypeHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ParticleTypeHolderSerializer extends AbstractScreamingSerializer implements TypeSerializer<ParticleTypeHolder> {
    public static final ParticleTypeHolderSerializer INSTANCE = new ParticleTypeHolderSerializer();

    @Override
    public ParticleTypeHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return ParticleTypeHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable ParticleTypeHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.platformName());
    }
}
