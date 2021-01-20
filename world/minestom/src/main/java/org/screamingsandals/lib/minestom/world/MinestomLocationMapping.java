package org.screamingsandals.lib.minestom.world;

import net.minestom.server.MinecraftServer;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapping;

public class MinestomLocationMapping extends LocationMapping {

    public static void init() {
        LocationMapping.init(MinestomLocationMapping::new);
    }

    public MinestomLocationMapping() {
        platform = Platform.JAVA_FLATTENING;

        converter.registerW2P(InstancedPosition.class, holder -> {
            final var instance = MinecraftServer.getInstanceManager().getInstances()
                    .stream()
                    .filter(next -> next.getUniqueId().equals(holder.getWorldId()))
                    .findAny();

            if (instance.isEmpty()) {
                return null;
            }

            return new InstancedPosition(instance.get(), (float) holder.getX(), (float) holder.getY(), (float) holder.getZ(),
                    holder.getYaw(), holder.getPitch());
        }).registerP2W(InstancedPosition.class, location ->
                new LocationHolder(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), location.getInstance().getUniqueId()));
    }
}
