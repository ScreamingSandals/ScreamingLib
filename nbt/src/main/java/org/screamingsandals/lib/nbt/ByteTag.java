package org.screamingsandals.lib.nbt;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class ByteTag implements Tag, NumericTag {
    private final byte value;

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public byte byteValue() {
        return value;
    }

    @Override
    public short shortValue() {
        return value;
    }

    @Override
    public boolean booleanValue() {
        return value != 0;
    }
}
