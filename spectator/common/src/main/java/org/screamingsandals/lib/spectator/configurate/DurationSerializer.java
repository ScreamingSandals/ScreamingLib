package org.screamingsandals.lib.spectator.configurate;

import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.function.Predicate;

public class DurationSerializer extends ScalarSerializer<Duration> {
    public static final DurationSerializer INSTANCE = new DurationSerializer();

    protected DurationSerializer() {
        super(Duration.class);
    }

    @Override
    public Duration deserialize(Type type, Object obj) throws SerializationException {
        if (obj instanceof CharSequence) {
            try {
                var value = obj.toString();
                if (!value.startsWith("P") && !value.startsWith("p")) {
                    value = "P" + value;
                }

                return Duration.parse(value);
            } catch (Throwable ex) {
                throw new SerializationException(ex);
            }
        }
        throw new SerializationException("Value was not of appropriate type");
    }

    @Override
    protected Object serialize(Duration item, Predicate<Class<?>> typeSupported) {
        return item.toString();
    }
}
