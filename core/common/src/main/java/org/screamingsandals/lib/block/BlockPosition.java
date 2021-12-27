package org.screamingsandals.lib.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.Server;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BlockPosition {
    protected int x;
    protected int y;
    protected int z;

    public static long toLong(int x, int y, int z) {
        if (Server.isVersion(1, 14)) {
            return ((long) (x & 0x3FFFFFF) << 38) | ((long) (z & 0x3FFFFFF) << 12) | (y & 0xFFF);
        }
        return ((long) (x & 0x3FFFFFF) << 38) | ((long) (y & 0xFFF) << 26) | (z & 0x3FFFFFF);
    }

    public long asLong() {
        return toLong(getX(), getY(), getZ());
    }
}
