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
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.world.dimension.DimensionMapping;

import java.util.Arrays;

@Service
public class BukkitDimensionMapping extends DimensionMapping {
    public BukkitDimensionMapping() {
        dimensionConverter
                .registerP2W(World.Environment.class, BukkitDimensionHolder::new);

        Arrays.stream(World.Environment.values()).forEach(environment -> {
            var holder = new BukkitDimensionHolder(environment);
            mapping.put(NamespacedMappingKey.of(environment.name()), holder);
            values.add(holder);
        });
    }
}
