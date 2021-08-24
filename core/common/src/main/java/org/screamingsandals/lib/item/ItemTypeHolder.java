package org.screamingsandals.lib.item;

import lombok.Data;
import org.screamingsandals.lib.utils.ComparableWrapper;

import java.util.Arrays;

@Data
public class ItemTypeHolder implements ComparableWrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return null;
    }

    @Override
    public boolean is(Object object) {
        return false;
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
