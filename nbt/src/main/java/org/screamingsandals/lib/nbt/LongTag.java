package org.screamingsandals.lib.nbt;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public final class LongTag implements Tag, NumericTag {
    private final long value;

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public byte byteValue() {
        return (byte) value;
    }

    @Override
    public short shortValue() {
        return (short) value;
    }

    @Override
    public boolean booleanValue() {
        return value != 0;
    }
}
