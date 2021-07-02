package org.screamingsandals.lib.bukkit.hologram.nms;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EquipmentSlot;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.entity.ArmorStandNMS;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.Reflect;

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

    public void setRotation(Vector3Df vector3Df) {
        Preconditions.checkNotNull(vector3Df, "Vector is null!");
        Reflect.getMethod(ClassStorage.NMS.EntityArmorStand, "setHeadPose,func_175415_a", ClassStorage.NMS.Vector3f).invokeInstance(handler, ClassStorage.getVectorToNMS(vector3Df));
    }

    public Vector3Df getRotation() {
        return ClassStorage.getVectorFromNMS(Reflect.getField(handler, "headPose,field_175443_bh,cg"));
    }

    public Object getHeadSlot() {
        return Reflect.getMethod(ClassStorage.NMS.CraftEquipmentSlot, "getNMS", EquipmentSlot.class).invokeStatic(EquipmentSlot.HEAD);
    }
}
