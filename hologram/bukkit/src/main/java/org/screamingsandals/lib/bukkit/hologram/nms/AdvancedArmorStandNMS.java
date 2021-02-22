package org.screamingsandals.lib.bukkit.hologram.nms;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.entity.ArmorStandNMS;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.utils.math.Vector3Df;

@Slf4j
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

    public void setItem(Item item) {
        ClassStorage.getMethod(ClassStorage.NMS.Entity, "setSlot", ClassStorage.NMS.EnumItemSlot, ClassStorage.NMS.ItemStack).invokeInstance(
                handler, getHeadSlot(), stackAsNMS(item));
        log.trace("Get Item: {}", getItem().toString());
    }

    public Item getItem() {
        return stackFromNMS(ClassStorage.getMethod(ClassStorage.NMS.EntityLiving, "getEquipment", ClassStorage.NMS.EnumItemSlot).invokeInstance(handler, getHeadSlot()));
    }

    public void setRotation(Vector3Df vector3Df) {
        Preconditions.checkNotNull(vector3Df, "Vector is null!");
        ClassStorage.getMethod(ClassStorage.NMS.EntityArmorStand, "setHeadPose", ClassStorage.NMS.Vector3f).invokeInstance(handler, ClassStorage.getVectorToNMS(vector3Df));
    }

    public Vector3Df getRotation() {
        return ClassStorage.getVectorFromNMS(ClassStorage.getField(handler, "headPose"));
    }

    private Object stackAsNMS(Item item) {
        Preconditions.checkNotNull(item, "Item is null!");
        return ClassStorage.getMethod(ClassStorage.NMS.CraftItemStack, "asNMSCopy", ItemStack.class).invokeStatic(item.as(ItemStack.class));
    }

    private Item stackFromNMS(Object item) {
        Preconditions.checkNotNull(item, "Item is null!");
        return ItemFactory.readStack(ClassStorage.getMethod(ClassStorage.NMS.CraftItemStack, "asBukkitCopy", ClassStorage.NMS.ItemStack).invokeStatic(item)).orElseThrow();
    }

    private Object getHeadSlot() {
        return ClassStorage.getMethod(ClassStorage.NMS.CraftEquipmentSlot, "getNMS", EquipmentSlot.class).invokeStatic(EquipmentSlot.HEAD);
    }
}
