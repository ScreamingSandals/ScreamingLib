package org.screamingsandals.lib.config.custom;

import java.io.Serializable;
import java.util.Map;

public interface ScreamingConfig extends Serializable {

    void registerValue(ValueHolder<?> value);

    void unregisterValue(String key);

    void unregisterValue(ValueHolder<?> value);

    void replaceValue(ValueHolder<?> value);

    ValueHolder<?> getValueHolder(String key);

    <T extends ValueHolder<T>> T get(String key);

    <T extends ValueHolder<T>> T get(String key, T def);

    Map<String, ValueHolder<?>> getValues();
}
