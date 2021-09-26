package org.screamingsandals.lib.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BlockPosition {
    protected int x;
    protected int y;
    protected int z;

    // TODO:
    public static long toLong(int x, int y, int z) {
        return 0;
    }

    public long asLong() {
        return toLong(getX(), getY(), getZ());
    }
}
