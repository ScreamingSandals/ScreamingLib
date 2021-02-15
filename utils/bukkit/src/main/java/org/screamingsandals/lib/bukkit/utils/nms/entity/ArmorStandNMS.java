package org.screamingsandals.lib.bukkit.utils.nms.entity;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;


public class ArmorStandNMS extends EntityNMS {

	public ArmorStandNMS(Object handler) {
		super(handler);
		if (!ClassStorage.NMS.EntityArmorStand.isInstance(handler)) {
			throw new IllegalArgumentException("Entity must be instance of EntityArmorStand!!");
		}
	}
	
	public ArmorStandNMS(ArmorStand stand) {
		this(ClassStorage.getHandle(stand));
	}
	
	public ArmorStandNMS(Location loc) throws Throwable {
		this(ClassStorage.NMS.EntityArmorStand.getConstructor(ClassStorage.NMS.World, double.class, double.class, double.class)
					.newInstance(ClassStorage.getMethod(loc.getWorld(), "getHandle").invoke(), loc.getX(), loc.getY(), loc.getZ()));
		this.setLocation(loc); // Update rotation
	}
	
	public void setSmall(boolean small) {
		ClassStorage.getMethod(handler, "setSmall,func_175420_a", boolean.class).invoke(small);
	}
	
	public boolean isSmall() {
		return (boolean) ClassStorage.getMethod(handler, "isSmall,func_175410_n").invoke();
	}
	
	public void setArms(boolean arms) {
		ClassStorage.getMethod(handler, "setArms,func_175413_k", boolean.class).invoke(arms);
	}
	
	public boolean hasArms() {
		return (boolean) ClassStorage.getMethod(handler, "hasArms,func_175402_q").invoke();
	}
	
	public void setBasePlate(boolean basePlate) {
		ClassStorage.getMethod(handler, "setBasePlate,func_175426_l", boolean.class).invoke(basePlate);
	}
	
	public boolean hasBasePlate() {
		return (boolean) ClassStorage.getMethod(handler, "hasBasePlate,func_175414_r").invoke();
	}
	
	public void setMarker(boolean marker) {
		ClassStorage.getMethod(handler, "setMarker,func_181027_m,n", boolean.class).invoke(marker);
	}
	
	public boolean isMarker() {
		return (boolean) ClassStorage.getMethod(handler, "isMarker,func_175426_l,s").invoke();
	}

}
