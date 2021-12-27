package org.screamingsandals.lib.bukkit.world.dimension;

import org.bukkit.World;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.world.dimension.DimensionMapping;

import java.util.Arrays;

@Service
public class BukkitDimensionMapping extends DimensionMapping {
    public BukkitDimensionMapping() {
        dimensionConverter
                .registerP2W(World.Environment.class, BukkitDimensionHolder::new);

        Arrays.stream(World.Environment.values()).forEach(environment -> {
            var holder = new BukkitDimensionHolder(environment);
            mapping.put(NamespacedMappingKey.of(environment.name()), holder);
            values.add(holder);
        });
    }
}
