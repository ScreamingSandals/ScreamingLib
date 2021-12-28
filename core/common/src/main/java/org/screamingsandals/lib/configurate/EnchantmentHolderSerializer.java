package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class EnchantmentHolderSerializer extends AbstractScreamingSerializer implements TypeSerializer<EnchantmentHolder> {
    public static final EnchantmentHolderSerializer INSTANCE = new EnchantmentHolderSerializer();

    @Override
    public EnchantmentHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            if (node.isMap()) {
                var typeNode = node.node("type").getString();
                var levelNode = node.node("level").getInt(1);

                if (typeNode != null && !typeNode.isEmpty()) {
                    return EnchantmentHolder.of(typeNode).withLevel(levelNode);
                }
            }

            // TODO: read Map.Entry? Is it possible?

            return EnchantmentHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable EnchantmentHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : (obj.platformName() + " " + obj.level()));
    }
}
