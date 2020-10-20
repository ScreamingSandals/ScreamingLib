package org.screamingsandals.lib.core.util.result;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder(access = AccessLevel.PACKAGE)
@Getter
public class SimpleResult implements Result {
    private final ResultState state;
    private final String message;
    private final Throwable throwable;
    private final Object data;

    @Override
    public <T> Optional<T> getData(Class<T> tClass) {
        if (data.getClass().isAssignableFrom(tClass)) {
            return Optional.of(tClass.cast(data));
        }
        return Optional.empty();
    }
}
