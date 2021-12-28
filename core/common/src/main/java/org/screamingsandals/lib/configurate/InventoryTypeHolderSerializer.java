package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class InventoryTypeHolderSerializer implements TypeSerializer<InventoryTypeHolder> {
    public static final InventoryTypeHolderSerializer INSTANCE = new InventoryTypeHolderSerializer();

    @Override
    public InventoryTypeHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return InventoryTypeHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable InventoryTypeHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.platformName());
    }
}
