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

package org.screamingsandals.lib.minestom.entity.pose;

import net.minestom.server.entity.Entity;
import org.screamingsandals.lib.entity.pose.EntityPoseMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;
import java.util.Locale;

@Service
public class MinestomEntityPoseMapping extends EntityPoseMapping {
    public MinestomEntityPoseMapping() {
        entityPoseConverter
                .registerP2W(Entity.Pose.class, MinestomEntityPoseHolder::new)
                .registerW2P(Entity.Pose.class, entityPoseHolder -> Entity.Pose.valueOf(entityPoseHolder.platformName()));

        Arrays.stream(Entity.Pose.values()).forEach(pose -> {
            final var holder = new MinestomEntityPoseHolder(pose);
            mapping.put(NamespacedMappingKey.of(pose.name().toLowerCase(Locale.ROOT)), holder);
            values.add(holder);
        });
    }
}
