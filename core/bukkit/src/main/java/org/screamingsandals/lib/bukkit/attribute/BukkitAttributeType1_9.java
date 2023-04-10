/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.attribute;

import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class BukkitAttributeType1_9 extends BasicWrapper<Attribute> implements AttributeType {

    public BukkitAttributeType1_9(@NotNull Attribute wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof Attribute || object instanceof AttributeType) {
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
        return getLocation(wrappedObject);
    }

    public static @NotNull ResourceLocation getLocation(@NotNull Attribute attribute) {
        var name = attribute.name();
        if (name.startsWith("GENERIC_")) {
            return ResourceLocation.of("generic." + name.substring(8));
        } else if (name.startsWith("HORSE_")) {
            return ResourceLocation.of("horse." + name.substring(6));
        } else if (name.startsWith("ZOMBIE_")) {
            return ResourceLocation.of("zombie." + name.substring(7));
        }
        return ResourceLocation.of(name);
    }
}
