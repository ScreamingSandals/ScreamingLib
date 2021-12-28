package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.stream.Collectors;

public class BlockTypeHolderSerializer extends AbstractScreamingSerializer implements TypeSerializer<BlockTypeHolder> {
    public static final BlockTypeHolderSerializer INSTANCE = new BlockTypeHolderSerializer();

    @Override
    public BlockTypeHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return BlockTypeHolder.of(type);
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable BlockTypeHolder obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        if (Server.isVersion(1, 13)) {
            var builder = new StringBuilder(obj.platformName());
            var data = obj.flatteningData();
            if (!data.isEmpty()) {
                builder.append('[');
                builder.append(data
                        .entrySet()
                        .stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.joining(",")));
                builder.append(']');
            }
            node.set(builder.toString());
        } else {
            node.set(obj.platformName() + ":" + obj.legacyData());
        }
    }
}
