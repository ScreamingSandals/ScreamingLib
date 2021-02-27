package org.screamingsandals.lib.utils.math;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vector3D implements Cloneable {
    private double x;
    private double y;
    private double z;

    public Vector3D add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public double distance(@NotNull Vector3D o) {
        return Math.sqrt(distanceSquared(o));
    }
    public double distanceSquared(@NotNull Vector3D o) {
        var deltaX = x - o.x;
        var deltaY = y - o.y;
        var deltaZ = z - o.z;

        return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
    }

    @NotNull
    public Vector3D normalize() {
        var length = length();

        x /= length;
        y /= length;
        z /= length;

        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Vector3D clone() {
        return new Vector3D(x, y, z);
    }
}
