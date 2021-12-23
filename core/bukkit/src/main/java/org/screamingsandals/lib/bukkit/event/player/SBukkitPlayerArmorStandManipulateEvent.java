package org.screamingsandals.lib.bukkit.event.player;

import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.SPlayerArmorStandManipulateEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;

public class SBukkitPlayerArmorStandManipulateEvent extends SBukkitPlayerInteractEntityEvent implements SPlayerArmorStandManipulateEvent {
    public SBukkitPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent event) {
        super(event);
    }

    // Internal cache
    private Item playerItem;
    private Item armorStandItem;
    private EquipmentSlotHolder slot;

    @Override
    public Item getPlayerItem() {
        if (playerItem == null) {
            playerItem = new BukkitItem(getEvent().getPlayerItem());
        }
        return playerItem;
    }

    @Override
    public Item getArmorStandItem() {
        if (armorStandItem == null) {
            armorStandItem = new BukkitItem(getEvent().getArmorStandItem());
        }
        return armorStandItem;
    }

    @Override
    public EquipmentSlotHolder getSlot() {
        if (slot == null) {
            slot = EquipmentSlotHolder.of(getEvent().getSlot());
        }
        return slot;
    }

    @Override
    public PlayerArmorStandManipulateEvent getEvent() {
        return (PlayerArmorStandManipulateEvent) super.getEvent();
    }
}
