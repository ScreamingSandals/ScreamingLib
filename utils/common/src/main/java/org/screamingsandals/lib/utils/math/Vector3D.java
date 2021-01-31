package org.screamingsandals.lib.utils.math;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vector3D implements Cloneable {
    private double x;
    private double y;
    private double z;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Vector3D clone() {
        return new Vector3D(x, y, z);
    }
}
