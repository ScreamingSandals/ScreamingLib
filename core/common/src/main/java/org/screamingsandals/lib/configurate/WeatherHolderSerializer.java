package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.world.weather.WeatherHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class WeatherHolderSerializer extends AbstractScreamingSerializer implements TypeSerializer<WeatherHolder> {

    public static final WeatherHolderSerializer INSTANCE = new WeatherHolderSerializer();

    @Override
    public WeatherHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return WeatherHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable WeatherHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.platformName());
    }
}
