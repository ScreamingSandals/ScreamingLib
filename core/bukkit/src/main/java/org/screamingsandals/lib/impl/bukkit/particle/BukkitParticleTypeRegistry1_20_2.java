/*
 * Copyright 2026 ScreamingSandals
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

import org.bukkit.Particle;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.utils.BukkitRegistry;
import org.screamingsandals.lib.impl.particle.ParticleTypeRegistry;
import org.screamingsandals.lib.particle.ParticleType;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public class BukkitParticleTypeRegistry1_20_2 extends ParticleTypeRegistry {
    public BukkitParticleTypeRegistry1_20_2() {
        specialType(Particle.class, BukkitParticleType1_9::new);
    }

    @Override
    protected @Nullable ParticleType resolveMappingPlatform(@NotNull ResourceLocation location) {
        return BukkitRegistry.tryObtainItem(Registry.PARTICLE_TYPE, BukkitParticleType1_9::new, location);
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull ParticleType> getRegistryItemStream0() {
        return BukkitRegistry.registryStream(Registry.PARTICLE_TYPE, BukkitParticleType1_9::new);
    }

}
