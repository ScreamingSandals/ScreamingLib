package org.screamingsandals.lib.bukkit.world.dimension;

import org.bukkit.World;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.world.dimension.DimensionHolder;
import org.screamingsandals.lib.world.dimension.DimensionMapping;

import java.util.Arrays;

@Service
public class BukkitDimensionMapping extends DimensionMapping {
    public BukkitDimensionMapping() {
        dimensionConverter
                .registerP2W(World.Environment.class, environment -> new DimensionHolder(environment.name()))
                .registerW2P(World.Environment.class, dimensionHolder -> World.Environment.valueOf(dimensionHolder.getPlatformName()));

        Arrays.stream(World.Environment.values()).forEach(environment -> {
            var holder = new DimensionHolder(environment.name());
            mapping.put(NamespacedMappingKey.of(environment.name()), holder);
            values.add(holder);
        });
    }
}
