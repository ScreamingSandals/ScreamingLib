package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class EquipmentSlotHolderSerializer extends AbstractScreamingSerializer implements TypeSerializer<EquipmentSlotHolder> {
    public static final EquipmentSlotHolderSerializer INSTANCE = new EquipmentSlotHolderSerializer();

    @Override
    public EquipmentSlotHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return EquipmentSlotHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable EquipmentSlotHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.platformName());
    }
}
