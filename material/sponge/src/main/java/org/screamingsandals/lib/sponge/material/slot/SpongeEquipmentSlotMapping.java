package org.screamingsandals.lib.sponge.material.slot;

import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.registry.RegistryTypes;

@Service
public class SpongeEquipmentSlotMapping extends EquipmentSlotMapping {
    public static void init() {
        EquipmentSlotMapping.init(SpongeEquipmentSlotMapping::new);
    }

    public SpongeEquipmentSlotMapping() {
        equipmentSlotConverter
                .registerW2P(EquipmentType.class, e -> Sponge.getGame().registries().registry(RegistryTypes.EQUIPMENT_TYPE).findEntry(ResourceKey.resolve(e.getPlatformName())).orElseThrow().value())
                .registerP2W(EquipmentType.class, e -> new EquipmentSlotHolder(Sponge.getGame().registries().registry(RegistryTypes.EQUIPMENT_TYPE).findValueKey(e).orElseThrow().getFormatted()));

        Sponge.getGame().registries().registry(RegistryTypes.EQUIPMENT_TYPE).forEach(itemType ->
                mapping.put(itemType.key().getNamespace().equals(ResourceKey.SPONGE_NAMESPACE) ? itemType.key().getValue().toUpperCase() : itemType.key().getFormatted().toUpperCase(), new EquipmentSlotHolder(itemType.key().getFormatted()))
        );
    }
}
