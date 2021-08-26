package org.screamingsandals.lib.sponge.material.slot;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.sponge.utils.SpongeRegistryMapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.registry.RegistryType;
import org.spongepowered.api.registry.RegistryTypes;

@Service
public class SpongeEquipmentSlotMapping extends EquipmentSlotMapping implements SpongeRegistryMapper<EquipmentType> {
    public static void init() {
        EquipmentSlotMapping.init(SpongeEquipmentSlotMapping::new);
    }

    public SpongeEquipmentSlotMapping() {
        equipmentSlotConverter
                .registerW2P(EquipmentType.class, e -> getEntry(e.getPlatformName()).value())
                .registerP2W(EquipmentType.class, e -> new EquipmentSlotHolder(getKeyByValue(e).getFormatted()));

        getAllKeys().forEach(key ->
                mapping.put(key.getNamespace().equals(ResourceKey.SPONGE_NAMESPACE) ? key.getValue().toUpperCase() : key.getFormatted().toUpperCase(), new EquipmentSlotHolder(key.getFormatted()))
        );
    }

    @Override
    @NotNull
    public RegistryType<EquipmentType> getRegistryType() {
        return RegistryTypes.EQUIPMENT_TYPE;
    }
}
