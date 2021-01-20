package org.screamingsandals.lib.world;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.MathUtils;
import org.screamingsandals.lib.utils.Wrapper;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigSerializable
public class LocationHolder implements Wrapper {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private UUID worldId;

    public LocationHolder(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public <T> T as(Class<T> type) {
        return LocationMapping.convert(this, type);
    }

    public double getDistanceSquared(@NotNull LocationHolder holder) {
        return MathUtils.square(getX() - holder.getX()) +
                MathUtils.square(getY() - holder.getY()) +
                MathUtils.square(getZ() - holder.getZ());
    }
}
