package org.screamingsandals.lib.minestom.material.attribute;

import net.minestom.server.attribute.AttributeInstance;
import net.minestom.server.attribute.AttributeModifier;
import net.minestom.server.attribute.AttributeOperation;
import org.screamingsandals.lib.material.attribute.AttributeHolder;
import org.screamingsandals.lib.material.attribute.AttributeMapping;
import org.screamingsandals.lib.material.attribute.AttributeModifierHolder;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Optional;

@Service
public class MinestomAttributeMapping extends AttributeMapping {
    public static void init() {
        AttributeMapping.init(MinestomAttributeMapping::new);
    }

    public MinestomAttributeMapping() {
        attributeModifierConverter
                .registerW2P(AttributeModifier.class, holder -> new AttributeModifier(
                        holder.getUuid(),
                        holder.getName(),
                        (float) holder.getAmount(),
                        AttributeOperation.valueOf(holder.getOperation().name())
                ))
                .registerP2W(AttributeModifier.class, attributeModifier -> new AttributeModifierHolder(
                        attributeModifier.getId(),
                        attributeModifier.getName(),
                        attributeModifier.getAmount(),
                        AttributeModifierHolder.Operation.valueOf(attributeModifier.getOperation().name()),
                        null
                ));
    }

    @Override
    protected Optional<AttributeHolder> wrapAttribute0(Object attribute) {
        if (attribute instanceof AttributeInstance) {
            return Optional.of(new MinestomAttributeHolder((AttributeInstance) attribute));
        }
        return Optional.empty();
    }
}
