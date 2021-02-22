package org.screamingsandals.lib.utils.math;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
