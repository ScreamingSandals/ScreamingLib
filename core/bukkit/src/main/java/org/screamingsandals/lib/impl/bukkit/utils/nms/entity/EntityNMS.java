/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.utils.nms.entity;

import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.nms.accessors.EntityAccessor;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class EntityNMS {
	protected Object handler;

	protected EntityNMS() {}

	public Object getDataWatcher() {
		return Reflect.fastInvoke(handler, EntityAccessor.METHOD_GET_ENTITY_DATA.get());
	}

	public void setCustomName(Component name) {
		final var method = EntityAccessor.METHOD_SET_CUSTOM_NAME_1.get();
		if (method != null) {
			Reflect.fastInvoke(method, ClassStorage.asMinecraftComponent(name));
		} else {
			Reflect.fastInvoke(handler, EntityAccessor.METHOD_SET_CUSTOM_NAME.get(), name.toLegacy());
		}
	}

	public void setInvisible(boolean invisible) {
		Reflect.fastInvoke(handler, EntityAccessor.METHOD_SET_INVISIBLE.get(), invisible);
	}
}
