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
