package org.screamingsandals.lib.nbt;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class BooleanTag implements Tag, NumericTag {
    private final boolean value;

    @Override
    public int intValue() {
        return value ? 1 : 0;
    }

    @Override
    public long longValue() {
        return value ? 1L : 0L;
    }

    @Override
    public float floatValue() {
        return value ? 1F : 0F;
    }

    @Override
    public double doubleValue() {
        return value ? 1.0 : 0.0;
    }

    @Override
    public byte byteValue() {
        return value ? (byte) 1 : (byte) 0;
    }

    @Override
    public short shortValue() {
        return value ? (short) 1 : (short) 0;
    }

    @Override
    public boolean booleanValue() {
        return value;
    }
}
