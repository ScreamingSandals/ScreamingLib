package org.screamingsandals.lib.attribute;

import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@AbstractService
public abstract class AttributeMapping {
    private static AttributeMapping attributeMapping;
    private static final Function<ConfigurationNode, AttributeModifierHolder> CONFIGURATE_LOAD_MODIFIER = node -> {
        var uuid = node.node("uuid");
        var name = node.node("name");
        var amount = node.node("amount");
        var operation = node.node("operation");

        try {
            return new AttributeModifierHolder(
                    uuid.get(UUID.class, (Supplier<UUID>) UUID::randomUUID),
                    name.getString(""),
                    amount.getDouble(),
                    operation.get(AttributeModifierHolder.Operation.class)
            );
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        return null;
    };
    private static final Function<ConfigurationNode, ItemAttributeHolder> CONFIGURATE_LOAD_ITEM = node -> {
        var type = node.node("type");
        var uuid = node.node("uuid");
        var name = node.node("name");
        var amount = node.node("amount");
        var operation = node.node("operation");
        var slot = node.node("slot");

        var typeOpt = AttributeTypeMapping.resolve(type.raw());

        if (typeOpt.isEmpty()) {
            return null;
        }

        try {
            return new ItemAttributeHolder(
                    typeOpt.get(),
                    uuid.get(UUID.class, (Supplier<UUID>) UUID::randomUUID),
                    name.getString(""),
                    amount.getDouble(),
                    operation.get(AttributeModifierHolder.Operation.class),
                    EquipmentSlotMapping.resolve(slot.raw()).orElse(null) // nullable
            );
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        return null;
    };

    protected final BidirectionalConverter<AttributeModifierHolder> attributeModifierConverter = BidirectionalConverter.<AttributeModifierHolder>build()
            .registerP2W(ConfigurationNode.class, CONFIGURATE_LOAD_MODIFIER)
            .registerP2W(Map.class, map -> {
                try {
                    return CONFIGURATE_LOAD_MODIFIER.apply(BasicConfigurationNode.root().set(map));
                } catch (ConfigurateException ignored) {
                    return null;
                }
            });

    protected final BidirectionalConverter<ItemAttributeHolder> itemAttributeConverter = BidirectionalConverter.<ItemAttributeHolder>build()
            .registerP2W(ConfigurationNode.class, CONFIGURATE_LOAD_ITEM)
            .registerP2W(Map.class, map -> {
                try {
                    return CONFIGURATE_LOAD_ITEM.apply(BasicConfigurationNode.root().set(map));
                } catch (ConfigurateException ignored) {
                    return null;
                }
            });

    public static void init(Supplier<AttributeMapping> supplier) {
        if (attributeMapping != null) {
            throw new UnsupportedOperationException("AttributeMapping is already initialized.");
        }

        attributeMapping = supplier.get();
    }

    public static Optional<AttributeHolder> wrapAttribute(Object attribute) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.wrapAttribute0(attribute);
    }

    protected abstract Optional<AttributeHolder> wrapAttribute0(Object attribute);

    public static Optional<AttributeModifierHolder> wrapAttributeModifier(Object attributeModifier) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.attributeModifierConverter.convertOptional(attributeModifier);
    }

    public static Optional<ItemAttributeHolder> wrapItemAttribute(Object attribute) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.itemAttributeConverter.convertOptional(attribute);
    }

    public static <T> T convertItemAttributeHolder(ItemAttributeHolder holder, Class<T> newType) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.itemAttributeConverter.convert(holder, newType);
    }

    public static <T> T convertAttributeModifierHolder(AttributeModifierHolder holder, Class<T> newType) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.attributeModifierConverter.convert(holder, newType);
    }

    public static boolean isInitialized() {
        return attributeMapping != null;
    }
}
