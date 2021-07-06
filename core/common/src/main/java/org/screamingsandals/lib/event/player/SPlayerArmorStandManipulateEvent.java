package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@Getter
public class SPlayerArmorStandManipulateEvent extends SPlayerInteractEntityEvent {
    private final Item playerItem;
    private final Item armorStandItem;
    private final EquipmentSlotHolder slot;

    public SPlayerArmorStandManipulateEvent(@NotNull final PlayerWrapper who, @NotNull final EntityBasic clickedEntity,
                                            @NotNull final Item playerItem, @NotNull final Item armorStandItem,
                                            @NotNull final EquipmentSlotHolder slot) {
        super(who, clickedEntity, slot);
        this.playerItem = playerItem;
        this.armorStandItem = armorStandItem;
        this.slot = slot;
    }
}
