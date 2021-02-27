package org.screamingsandals.lib.utils.math;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vector3Df implements Cloneable {
    private float x;
    private float y;
    private float z;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Vector3Df clone() {
        return new Vector3Df(x, y, z);
    }

    public Vector3Df add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    public float distance(@NotNull Vector3Df o) {
        return (float) Math.sqrt(distanceSquared(o));
    }
    public float distanceSquared(@NotNull Vector3Df o) {
        var deltaX = x - o.x;
        var deltaY = y - o.y;
        var deltaZ = z - o.z;

        return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
    }

    @NotNull
    public Vector3Df normalize() {
        var length = length();

        x /= length;
        y /= length;
        z /= length;

        return this;
    }
}
