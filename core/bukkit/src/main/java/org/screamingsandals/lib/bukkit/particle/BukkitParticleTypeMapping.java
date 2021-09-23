package org.screamingsandals.lib.bukkit.particle;

import org.bukkit.Particle;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.particle.*;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitParticleTypeMapping extends ParticleTypeMapping {
    public BukkitParticleTypeMapping() {
        particleTypeConverter
                .registerP2W(Particle.class, particle -> new ParticleTypeHolder(particle.name()))
                .registerW2P(Particle.class, particleHolder -> Particle.valueOf(particleHolder.getPlatformName()));

        Arrays.stream(Particle.values())
                .filter(particle -> !particle.name().startsWith("LEGACY_"))
                .forEach(particle -> {
                    var holder = new ParticleTypeHolder(particle.name());
                    mapping.put(NamespacedMappingKey.of(particle.name()), holder);
                    values.add(holder);
                });
    }

    @Override
    @Nullable
    protected Class<? extends ParticleData> getExpectedParticleDataClass0(ParticleTypeHolder particle) {
        var dataType = particle.as(Particle.class).getDataType();
        if (dataType != Void.class) {
            switch (dataType.getSimpleName()) {
                case "MaterialData":
                case "BlockData":
                    return BlockTypeHolder.class;
                case "ItemStack":
                    return Item.class;
                case "DustOptions":
                    return DustOptions.class;
                case "DustTransition":
                    return DustTransition.class;
            }
        }
        return null;
    }
}
