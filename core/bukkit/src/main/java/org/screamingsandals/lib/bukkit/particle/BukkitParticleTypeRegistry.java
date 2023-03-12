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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.particle.*;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.utils.registry.SimpleRegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitParticleTypeRegistry extends ParticleTypeRegistry {
    public BukkitParticleTypeRegistry() {
        specialType(Particle.class, BukkitParticleType::new);
    }

    // TODO: is there any bukkit-like server supporting custom values for this registry?
    @Override
    protected @Nullable ParticleType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        if (location.path().startsWith("LEGACY_")) {
            return null; // filter out legacy
        }

        try {
            var value = org.bukkit.Particle.valueOf(location.path().toUpperCase(Locale.ROOT));
            return new BukkitParticleType(value);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull ParticleType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(org.bukkit.Particle.values()).filter(particle -> !particle.name().startsWith("LEGACY_")),
                BukkitParticleType::new,
                particleType -> ResourceLocation.of(particleType.name()),
                (particleType, literal) -> particleType.name().toLowerCase(Locale.ROOT).contains(literal),
                (particleType, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }

}
