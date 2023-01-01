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

package org.screamingsandals.lib.bukkit.world.dimension;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.dimension.DimensionHolder;

import java.util.Arrays;

public class BukkitDimensionHolder extends BasicWrapper<World.Environment> implements DimensionHolder {

    public BukkitDimensionHolder(World.@NotNull Environment wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof World.Environment || object instanceof DimensionHolder) {
            return equals(object);
        }
        return equals(DimensionHolder.ofNullable(object));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
