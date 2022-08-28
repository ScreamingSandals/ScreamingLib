package org.screamingsandals.lib.nbt;

public interface NumericTag extends Tag {
    int intValue();

    long longValue();

    float floatValue();

    double doubleValue();

    byte byteValue();

    short shortValue();

    boolean booleanValue();
}
