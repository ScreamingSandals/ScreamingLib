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

package org.screamingsandals.lib.bukkit.container.type;

import org.bukkit.event.inventory.InventoryType;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitInventoryTypeHolder extends BasicWrapper<InventoryType> implements InventoryTypeHolder {

    public BukkitInventoryTypeHolder(InventoryType wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public int size() {
        return wrappedObject.getDefaultSize();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof InventoryType || object instanceof InventoryTypeHolder) {
            return equals(object);
        }
        return equals(InventoryTypeHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
