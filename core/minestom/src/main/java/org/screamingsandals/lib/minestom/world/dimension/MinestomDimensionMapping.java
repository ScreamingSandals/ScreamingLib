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

package org.screamingsandals.lib.minestom.world.dimension;

import net.minestom.server.MinecraftServer;
import net.minestom.server.world.DimensionType;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.world.dimension.DimensionMapping;

@Service
public class MinestomDimensionMapping extends DimensionMapping {
    public MinestomDimensionMapping() {
        dimensionConverter
                .registerP2W(DimensionType.class, MinestomDimensionHolder::new);

        MinecraftServer.getDimensionTypeManager().unmodifiableList().forEach(environment -> {
            final var holder = new MinestomDimensionHolder(environment);
            mapping.put(NamespacedMappingKey.of(environment.toString()), holder);
            values.add(holder);
        });
    }
}
