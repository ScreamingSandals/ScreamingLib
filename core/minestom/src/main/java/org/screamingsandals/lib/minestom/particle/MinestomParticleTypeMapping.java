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
