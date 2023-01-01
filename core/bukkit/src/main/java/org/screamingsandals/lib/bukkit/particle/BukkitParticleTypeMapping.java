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

package org.screamingsandals.lib.bukkit.particle;

import org.bukkit.Particle;
import org.screamingsandals.lib.particle.*;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitParticleTypeMapping extends ParticleTypeMapping {
    public BukkitParticleTypeMapping() {
        particleTypeConverter
                .registerP2W(Particle.class, BukkitParticleTypeHolder::new);

        Arrays.stream(Particle.values())
                .filter(particle -> !particle.name().startsWith("LEGACY_"))
                .forEach(particle -> {
                    var holder = new BukkitParticleTypeHolder(particle);
                    mapping.put(NamespacedMappingKey.of(particle.name()), holder);
                    values.add(holder);
                });
    }
}
