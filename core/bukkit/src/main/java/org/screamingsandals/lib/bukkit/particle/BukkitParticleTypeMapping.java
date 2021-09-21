package org.screamingsandals.lib.bukkit.particle;

import org.bukkit.Particle;
import org.screamingsandals.lib.particle.ParticleTypeHolder;
import org.screamingsandals.lib.particle.ParticleTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitParticleTypeMapping extends ParticleTypeMapping {
    public BukkitParticleTypeMapping() {
        particleTypeConverter
                .registerP2W(Particle.class, particle -> new ParticleTypeHolder(particle.name()))
                .registerW2P(Particle.class, particleHolder -> Particle.valueOf(particleHolder.getPlatformName()));

        Arrays.stream(Particle.values()).filter(particle -> !particle.name().startsWith("LEGACY_")).forEach(particle -> mapping.put(NamespacedMappingKey.of(particle.name()), new ParticleTypeHolder(particle.name())));
    }
}
