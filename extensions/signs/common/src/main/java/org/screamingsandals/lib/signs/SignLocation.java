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

package org.screamingsandals.lib.signs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.Worlds;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigSerializable
public class SignLocation implements Wrapper {
    private @NotNull String world;
    private double x;
    private double y;
    private double z;

    public SignLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == Location.class) {
            return (T) new Location(x, y, z, 0, 0, Objects.requireNonNull(Worlds.getWorld(world)));
        }
        throw new UnsupportedOperationException("Unsupported type!");
    }
}
