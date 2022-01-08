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

package org.screamingsandals.lib.bukkit.entity.type;

import org.bukkit.entity.EntityType;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitEntityTypeMapping extends EntityTypeMapping {
    public BukkitEntityTypeMapping() {
        entityTypeConverter
                .registerP2W(EntityType.class, BukkitEntityTypeHolder::new);

        Arrays.stream(EntityType.values()).forEach(entityType -> {
            var holder = new BukkitEntityTypeHolder(entityType);
            mapping.put(NamespacedMappingKey.of(entityType.name()), holder);
            values.add(holder);
        });
    }
}
