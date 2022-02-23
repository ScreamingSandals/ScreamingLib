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

package org.screamingsandals.lib.minestom.particle;

import net.minestom.server.particle.Particle;
import org.screamingsandals.lib.particle.ParticleTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@Service
public class MinestomParticleTypeMapping extends ParticleTypeMapping {
    public MinestomParticleTypeMapping() {
        particleTypeConverter
                .registerP2W(Particle.class, MinestomParticleTypeHolder::new);

        Particle.values().forEach(particle -> {
            final var holder = new MinestomParticleTypeHolder(particle);
            mapping.put(NamespacedMappingKey.of(particle.name()), holder);
            values.add(holder);
        });
    }
}
