/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.bukkit.utils.nms.entity;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ArmorStandAccessor;
import org.screamingsandals.lib.utils.reflect.Reflect;


public class ArmorStandNMS extends EntityNMS {

	public ArmorStandNMS(Object handler) {
		super(handler);
		if (!ArmorStandAccessor.getType().isInstance(handler)) {
			throw new IllegalArgumentException("Entity must be instance of EntityArmorStand!!");
		}
	}
	
	public ArmorStandNMS(ArmorStand stand) {
		this(ClassStorage.getHandle(stand));
	}
	
	public ArmorStandNMS(Location loc) {
		super(Reflect.construct(ArmorStandAccessor.getConstructor0(), ClassStorage.getHandle(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ()));
		this.setLocation(loc); // Update rotation
	}
	
	public void setSmall(boolean small) {
		Reflect.fastInvoke(handler, ArmorStandAccessor.getMethodSetSmall1(), small);
	}
	
	public boolean isSmall() {
		return (boolean) Reflect.fastInvoke(handler, ArmorStandAccessor.getMethodIsSmall1());
	}
	
	public void setArms(boolean arms) {
		Reflect.fastInvoke(handler, ArmorStandAccessor.getMethodSetShowArms1(), arms);
	}
	
	public boolean hasArms() {
		return (boolean) Reflect.fastInvoke(handler, ArmorStandAccessor.getMethodIsShowArms1());
	}
	
	public void setBasePlate(boolean basePlate) {
		Reflect.fastInvoke(handler, ArmorStandAccessor.getMethodSetNoBasePlate1(), basePlate);
	}
	
	public boolean hasBasePlate() {
		return (boolean) Reflect.fastInvoke(handler, ArmorStandAccessor.getMethodIsNoBasePlate1());
	}
	
	public void setMarker(boolean marker) {
		Reflect.fastInvoke(handler, ArmorStandAccessor.getMethodSetMarker1(), marker);
	}
	
	public boolean isMarker() {
		return (boolean)  Reflect.fastInvoke(handler, ArmorStandAccessor.getMethodIsMarker1());
	}
}
