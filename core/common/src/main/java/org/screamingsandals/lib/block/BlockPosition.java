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

    public static BlockPosition fromLong(long packedPos) {
        return new BlockPosition((int) (packedPos >> 38L), (int) (packedPos << 52L >> 52L), (int) (packedPos << 26L >> 38L));
    }

    public long asLong() {
        return toLong(getX(), getY(), getZ());
    }

    public static long toLong(int x, int y, int z) {
        return (x & 0x3FFFFFFL) << 38L | y & 0xFFFL | (z & 0x3FFFFFFL) << 12L;
    }
}
