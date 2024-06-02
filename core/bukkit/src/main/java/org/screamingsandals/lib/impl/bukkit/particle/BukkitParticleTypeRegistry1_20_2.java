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

package org.screamingsandals.lib.impl.bukkit.particle;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.particle.ParticleType;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.List;

@Service
public class BukkitParticleTypeRegistry1_20_2 extends BukkitParticleTypeRegistry {
    public BukkitParticleTypeRegistry1_20_2() {
        specialType(Particle.class, BukkitParticleType1_9::new);
    }

    @Override
    protected @Nullable ParticleType resolveMappingPlatform(@NotNull ResourceLocation location) {
        var particleType = Registry.PARTICLE_TYPE.get(new NamespacedKey(location.namespace(), location.path()));
        if (particleType != null ) {
            return new BukkitParticleType1_9(particleType);
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull ParticleType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Registry.PARTICLE_TYPE.stream().filter(particle -> !particle.name().startsWith("LEGACY_")),
                BukkitParticleType1_9::new,
                particleType -> {
                    var namespaced = particleType.getKey();
                    return ResourceLocation.of(namespaced.getNamespace(), namespaced.getKey());
                },
                (particleType, literal) -> particleType.getKey().getKey().contains(literal),
                (particleType, namespace) -> particleType.getKey().getNamespace().equals(namespace),
                List.of()
        );
    }

}
