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

package org.screamingsandals.lib.signs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.WorldMapper;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigSerializable
public class SignLocation implements Wrapper {
    private String world;
    private double x;
    private double y;
    private double z;

    public SignLocation(LocationHolder location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type == LocationHolder.class) {
            var holder = new LocationHolder(x, y, z);
            holder.setWorld(WorldMapper.getWorld(world).orElseThrow());
            return (T) holder;
        }
        throw new UnsupportedOperationException("Unsupported type!");
    }
}
