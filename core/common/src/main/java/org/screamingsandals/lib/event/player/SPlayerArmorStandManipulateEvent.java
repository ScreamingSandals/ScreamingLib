package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
@Getter
public class SPlayerArmorStandManipulateEvent extends SPlayerInteractEntityEvent {
    private final ImmutableObjectLink<Item> playerItem;
    private final ImmutableObjectLink<Item> armorStandItem;
    private final ImmutableObjectLink<EquipmentSlotHolder> slot;

    public SPlayerArmorStandManipulateEvent(ImmutableObjectLink<PlayerWrapper> who, ImmutableObjectLink<EntityBasic> clickedEntity,
                                            ImmutableObjectLink<Item> playerItem, ImmutableObjectLink<Item> armorStandItem,
                                            ImmutableObjectLink<EquipmentSlotHolder> slot, ImmutableObjectLink<EquipmentSlotHolder> hand) {
        super(who, clickedEntity, hand);
        this.playerItem = playerItem;
        this.armorStandItem = armorStandItem;
        this.slot = slot;
    }

    public Item getPlayerItem() {
        return playerItem.get();
    }

    public Item getArmorStandItem() {
        return armorStandItem.get();
    }

    public EquipmentSlotHolder getSlot() {
        return slot.get();
    }
}
