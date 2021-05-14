package org.screamingsandals.lib.bukkit;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutEntityEquipment;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.List;

public class BukkitSPacketPlayOutEntityEquipment extends BukkitSPacket implements SPacketPlayOutEntityEquipment {

    public BukkitSPacketPlayOutEntityEquipment() {
        super(ClassStorage.NMS.PacketPlayOutEntityEquipment);
    }

    public boolean isOldPacket() {
        return Reflect.getField(packet.getClass(), "d", packet) != null;
    }

    @Override
    public void setEntity(EntityBasic entity) {
        if (entity == null) {
            throw new UnsupportedOperationException("Entity cannot be null!");
        }

        packet.setField("a", entity.getEntityId());
    }

    @Override
    public void setItemAndSlot(Item item, Slot slot) {
        if (!isOldPacket()) {
            packet.setField("c", ClassStorage.stackAsNMS(item.as(ItemStack.class)));
            packet.setField("b", getSlot(slot));
        } else {
            packet.setField("b", List.of(Pair.of(getSlot(slot), ClassStorage.stackAsNMS(item.as(ItemStack.class)))));
        }
    }

    public Object getSlot(Slot slot) {
        EquipmentSlot bukkitSlot;
        switch (slot) {
            case FEET:
                bukkitSlot = EquipmentSlot.FEET;
                break;
            case HAND:
                bukkitSlot = EquipmentSlot.HAND;
                break;
            case OFF_HAND:
                bukkitSlot = EquipmentSlot.OFF_HAND;
                break;
            case HEAD:
                bukkitSlot = EquipmentSlot.HEAD;
                break;
            case LEGS:
                bukkitSlot = EquipmentSlot.LEGS;
                break;
            case CHEST:
                bukkitSlot = EquipmentSlot.CHEST;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + slot);
        }

        return Reflect.getMethod(ClassStorage.NMS.CraftEquipmentSlot, "getNMS", EquipmentSlot.class).invokeStatic(bukkitSlot);
    }
}
