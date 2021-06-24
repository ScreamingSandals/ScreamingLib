package org.screamingsandals.lib.world.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.WorldHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.UUID;

public class WorldHolderTypeSerializer implements TypeSerializer<WorldHolder> {
    private final String UUID_FIELD = "uuid";

    @Override
    public WorldHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return LocationMapper.getWorld(node.node(UUID_FIELD).get(UUID.class)).orElseThrow();
    }

    @Override
    public void serialize(Type type, @Nullable WorldHolder holder, ConfigurationNode node) throws SerializationException {
        if (holder == null) {
            node.raw(null);
            return;
        }
        node.node(UUID_FIELD).set(holder.getUuid());
    }
}
