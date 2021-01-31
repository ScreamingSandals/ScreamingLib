package org.screamingsandals.lib.material.attribute;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class AttributeMapping {
    private static AttributeMapping attributeMapping;
    private static final Function<ConfigurationNode, AttributeModifierHolder> CONFIGURATE_LOAD = node -> {
        return null; // TODO Configurate load
    };

    protected final BidirectionalConverter<AttributeModifierHolder> attributeModifierConverter = BidirectionalConverter.<AttributeModifierHolder>build()
            .registerP2W(ConfigurationNode.class, CONFIGURATE_LOAD)
            .registerP2W(Map.class, map -> {
                try {
                    return CONFIGURATE_LOAD.apply(BasicConfigurationNode.root().set(map));
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
