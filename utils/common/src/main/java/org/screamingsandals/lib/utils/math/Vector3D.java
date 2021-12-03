package org.screamingsandals.lib.utils.math;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.WrappedLocation;
import org.screamingsandals.lib.utils.WrappedVector3D;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Vector3D implements Cloneable {
    public static final Vector3D ZERO = new Vector3D(0,0,0);

    private double x;
    private double y;
    private double z;

    public static Vector3D unwrap(WrappedVector3D wrapped) {
        return new Vector3D(wrapped.getX(), wrapped.getY(), wrapped.getZ());
    }

    public static Vector3D unwrap(WrappedLocation wrapped) {
        return new Vector3D(wrapped.getX(), wrapped.getY(), wrapped.getZ());
    }

    public WrappedVector3D wrap() {
        return WrappedVector3D.newBuilder()
                .setX(this.x)
                .setY(this.y)
                .setZ(this.z)
                .build();
    }

    public Vector3D add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3D subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3D subtract(Vector3D vec) {
        this.x -= vec.getX();
        this.y -= vec.getY();
        this.z -= vec.getZ();
        return this;
    }

    public double dot(@NotNull Vector3D vector) {
        return x * vector.x + y * vector.y + z * vector.z;
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

    public Vector3D multiply(double multiply) {
        this.x *= multiply;
        this.y *= multiply;
        this.z *= multiply;

        return this;
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
