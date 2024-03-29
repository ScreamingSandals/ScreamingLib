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

package org.screamingsandals.lib.impl.bukkit.attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.impl.attribute.AttributeTypeRegistry;
import org.screamingsandals.lib.impl.nms.accessors.AttributeAccessor;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class BukkitAttributeType1_8 extends BasicWrapper<Object> implements AttributeType {

    public BukkitAttributeType1_8(@NotNull Object wrappedObject) {
        super(wrappedObject);
        if (AttributeAccessor.getType() == null || !AttributeAccessor.getType().isInstance(wrappedObject)) {
            throw new IllegalArgumentException("Object must be an instance of Attribute!");
        }
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.toString();
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (AttributeAccessor.getType().isInstance(object) || object instanceof AttributeType) {
            return equals(object);
        }
        return equals(AttributeType.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public @NotNull ResourceLocation location() {
        var registry = AttributeTypeRegistry.getInstance();
        if (registry instanceof BukkitAttributeTypeRegistry1_8) {
            var loc = ((BukkitAttributeTypeRegistry1_8) registry).attributesToLocation.get(wrappedObject);
            if (loc != null) {
                return loc;
            }
        }
        return ResourceLocation.of(wrappedObject.toString());
    }
}
