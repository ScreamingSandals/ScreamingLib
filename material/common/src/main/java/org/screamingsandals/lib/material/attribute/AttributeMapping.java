package org.screamingsandals.lib.material.attribute;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.Optional;
import java.util.function.Supplier;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class AttributeMapping {
    private static AttributeMapping attributeMapping;

    protected final BidirectionalConverter<AttributeModifierHolder> attributeModifierConverter = BidirectionalConverter.build();

    public static void init(Supplier<AttributeMapping> supplier) {
        if (attributeMapping != null) {
            throw new UnsupportedOperationException("AttributeMapping is already initialized.");
        }

        attributeMapping = supplier.get();
        attributeMapping.attributeModifierConverter.finish();
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
