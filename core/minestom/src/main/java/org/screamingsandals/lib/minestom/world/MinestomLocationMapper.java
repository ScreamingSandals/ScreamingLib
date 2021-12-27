package org.screamingsandals.lib.minestom.world;

import net.minestom.server.coordinate.Pos;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

@Service
public class MinestomLocationMapper extends LocationMapper {
    public MinestomLocationMapper() {
        converter.registerW2P(Pos.class, holder -> new Pos(holder.getX(), holder.getY(), holder.getZ(), holder.getYaw(), holder.getPitch()))
                .registerP2W(Pos.class, pos -> new LocationHolder(pos.x(), pos.y(), pos.z(), pos.yaw(), pos.pitch(), null));
    }
}
