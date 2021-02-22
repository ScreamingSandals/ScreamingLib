package org.screamingsandals.lib.bukkit.hologram.nms;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.entity.ArmorStandNMS;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.builder.ItemFactory;

public class AdvancedArmorStandNMS extends ArmorStandNMS {

    public AdvancedArmorStandNMS(Object handler) {
        super(handler);
    }

    public AdvancedArmorStandNMS(ArmorStand stand) {
        super(stand);
    }

    public AdvancedArmorStandNMS(Location loc) throws Throwable {
        super(loc);
    }

    public void setItem(EquipmentSlot slot, Item item) {
        ClassStorage.getMethod(ClassStorage.NMS.EntityLiving, "setSlot").invokeInstance(
                handler, equipmentSlotToNMS(slot), stackAsNMS(item), false);
    }


    public Item getItem(EquipmentSlot slot) {
        return stackFromNMS(ClassStorage.getMethod(ClassStorage.NMS.EntityLiving, "getEquipment").invokeInstance(handler, equipmentSlotToNMS(slot)));
    }

    private Object stackAsNMS(Item item) {
        return ClassStorage.getMethod(ClassStorage.NMS.CraftItemStack, "asNMSCopy").invokeInstance(item.as(ItemStack.class));
    }

    private Item stackFromNMS(Object item) {
        return ItemFactory.readStack(ClassStorage.getMethod(ClassStorage.NMS.CraftItemStack, "asBukkitCopy").invokeStatic(item)).orElseThrow();
    }

    private Object equipmentSlotToNMS(EquipmentSlot slot) {
        return ClassStorage.getMethod(ClassStorage.NMS.CraftEquipmentSlot, "getNMS").invokeStatic(slot);
    }
}
