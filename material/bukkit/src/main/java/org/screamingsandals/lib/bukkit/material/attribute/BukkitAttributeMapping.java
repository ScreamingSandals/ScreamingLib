package org.screamingsandals.lib.bukkit.material.attribute;

import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.screamingsandals.lib.bukkit.material.slot.BukkitEquipmentSlotMapping;
import org.screamingsandals.lib.material.attribute.AttributeHolder;
import org.screamingsandals.lib.material.attribute.AttributeMapping;
import org.screamingsandals.lib.material.attribute.AttributeModifierHolder;
import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.utils.InitUtils;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Optional;

@Service(dependsOn = {
        BukkitAttributeTypeMapping.class,
        BukkitEquipmentSlotMapping.class
})
public class BukkitAttributeMapping extends AttributeMapping {
    public static void init() {
        AttributeMapping.init(BukkitAttributeMapping::new);
    }

    public BukkitAttributeMapping() {
        InitUtils.doIfNot(BukkitAttributeTypeMapping::isInitialized, BukkitAttributeTypeMapping::init);
        InitUtils.doIfNot(BukkitEquipmentSlotMapping::isInitialized, BukkitEquipmentSlotMapping::init);

        attributeModifierConverter
                .registerW2P(AttributeModifier.class, holder -> {
                    try {
                        return new AttributeModifier(
                                holder.getUuid(),
                                holder.getName(),
                                holder.getAmount(),
                                AttributeModifier.Operation.values()[holder.getOperation().ordinal()],
                                holder.getSlot() != null ? holder.getSlot().as(EquipmentSlot.class) : null
                        );
                    } catch (Throwable throwable) {
                        return new AttributeModifier(
                                holder.getUuid(),
                                holder.getName(),
                                holder.getAmount(),
                                AttributeModifier.Operation.values()[holder.getOperation().ordinal()]
                        );
                    }
                })
                .registerP2W(AttributeModifier.class, attributeModifier -> {
                    try {
                        return new AttributeModifierHolder(
                                attributeModifier.getUniqueId(),
                                attributeModifier.getName(),
                                attributeModifier.getAmount(),
                                AttributeModifierHolder.Operation.byOrdinal(attributeModifier.getOperation().ordinal()),
                                EquipmentSlotMapping.resolve(attributeModifier.getSlot()).orElse(null) // nullable
                        );
                    } catch (Throwable throwable) {
                        return new AttributeModifierHolder(
                                attributeModifier.getUniqueId(),
                                attributeModifier.getName(),
                                attributeModifier.getAmount(),
                                AttributeModifierHolder.Operation.byOrdinal(attributeModifier.getOperation().ordinal()),
                                null
                        );
                    }
                });
    }

    @Override
    protected Optional<AttributeHolder> wrapAttribute0(Object attribute) {
        if (attribute instanceof AttributeInstance) {
            return Optional.of(new BukkitAttributeHolder((AttributeInstance) attribute));
        }
        return Optional.empty();
    }
}
