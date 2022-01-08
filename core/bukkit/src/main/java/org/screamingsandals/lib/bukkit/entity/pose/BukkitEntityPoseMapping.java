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

package org.screamingsandals.lib.bukkit.entity.pose;

import org.bukkit.entity.Pose;
import org.screamingsandals.lib.entity.pose.EntityPoseMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Arrays;

@Service
public class BukkitEntityPoseMapping extends EntityPoseMapping {
    public BukkitEntityPoseMapping() {
        if (Reflect.has("org.bukkit.entity.Pose")) {
            entityPoseConverter
                    .registerP2W(Pose.class, BukkitEntityPoseHolder::new);

            Arrays.stream(Pose.values()).forEach(pose -> {
                var holder = new BukkitEntityPoseHolder(pose);
                mapping.put(NamespacedMappingKey.of(pose.name()), holder);
                values.add(holder);
            });
        }
    }
}
