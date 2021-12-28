package org.screamingsandals.lib.slot;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.configurate.EquipmentSlotHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AbstractService
public abstract class EquipmentSlotMapping extends AbstractTypeMapper<EquipmentSlotHolder> {
    private static EquipmentSlotMapping equipmentSlotMapping;

    protected final BidirectionalConverter<EquipmentSlotHolder> equipmentSlotConverter = BidirectionalConverter.<EquipmentSlotHolder>build()
            .registerP2W(EquipmentSlotHolder.class, e -> e)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return EquipmentSlotHolderSerializer.INSTANCE.deserialize(EquipmentSlotHolder.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    @ApiStatus.Internal
    public EquipmentSlotMapping() {
        if (equipmentSlotMapping != null) {
            throw new UnsupportedOperationException("EquipmentSlotMapping is already initialized.");
        }

        equipmentSlotMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @OfMethodAlternative(value = EquipmentSlotHolder.class, methodName = "ofOptional")
    public static Optional<EquipmentSlotHolder> resolve(Object slot) {
        if (equipmentSlotMapping == null) {
            throw new UnsupportedOperationException("EquipmentSlotMapping is not initialized yet.");
        }

        if (slot == null) {
            return Optional.empty();
        }

        return equipmentSlotMapping.equipmentSlotConverter.convertOptional(slot).or(() -> equipmentSlotMapping.resolveFromMapping(slot));
    }

    @OfMethodAlternative(value = EquipmentSlotHolder.class, methodName = "all")
    public static List<EquipmentSlotHolder> getValues() {
        if (equipmentSlotMapping == null) {
            throw new UnsupportedOperationException("EquipmentSlotMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(equipmentSlotMapping.values);
    }

    @OnPostConstruct
    public void legacyMapping() {
        // Vanilla <-> Bukkit
        mapAlias("MAIN_HAND", "HAND");
        mapAlias("OFF_HAND", "OFF_HAND");
        mapAlias("BOOTS", "FEET");
        mapAlias("LEGGINGS", "LEGS");
        mapAlias("CHESTPLATE", "CHEST");
        mapAlias("HELMET", "HEAD");
    }
}
