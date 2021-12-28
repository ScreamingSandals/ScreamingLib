package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ItemTypeHolderSerializer extends AbstractScreamingSerializer implements TypeSerializer<ItemTypeHolder> {
    public static final ItemTypeHolderSerializer INSTANCE = new ItemTypeHolderSerializer();

    @Override
    public ItemTypeHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return ItemTypeHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable ItemTypeHolder obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }
        if (obj.forcedDurability() == 0) {
            node.set(obj.platformName());
        } else {
            node.set(obj.platformName() + ":" + obj.forcedDurability());
        }
    }
}
