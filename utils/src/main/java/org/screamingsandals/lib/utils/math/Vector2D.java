package org.screamingsandals.lib.utils.math;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vector2D implements Cloneable {
    private double x;
    private double y;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Vector2D clone() {
        return new Vector2D(x, y);
    }
}