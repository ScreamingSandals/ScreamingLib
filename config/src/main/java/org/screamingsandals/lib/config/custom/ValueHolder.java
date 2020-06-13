package org.screamingsandals.lib.config.custom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValueHolder<T> implements Serializable {
    private String key;
    private T value;
    private ValueType valueType;

    public static <T> ValueHolder<T> builder(String key, T value, ValueType valueType) {
        return new ValueHolder<>(key, value, valueType);
    }

    public static <T> ValueHolder<T> builder(String key, T value) {
        return new ValueHolder<>(key, value, ValueType.UNDEFINED);
    }

    public boolean getBoolean() {
        return (boolean) value;
    }

    public int getInt() {
        //due to GSON
        if (value instanceof Double) {
            return ((Double) value).intValue();
        }
        return (int) value;
    }

    public String getString() {
        return (String) value;
    }

    public double getDouble() {
        return (double) value;
    }

    public char getChar() {
        return (char) value;
    }

    @SuppressWarnings("unchecked")
    public List<T> getList() {
        try {
            return (List<T>) value;
        } catch (Exception e) {
            return null;
        }
    }
}