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

    public <T> Optional<T> getData() {
        return Optional.ofNullable((T) data);
    }

    @Override
    public <T> Optional<T> getData(Class<T> tClass) {
        if (data.getClass().isAssignableFrom(tClass)) {
            return Optional.of(tClass.cast(data));
        }
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> getDataIfOk(Class<T> tClass) {
        if (state == ResultState.OK) {
            return getData(tClass);
        }

        return Optional.empty();
    }

    @Override
    public boolean isData() {
        return data != null;
    }

    @Override
    public boolean isOk() {
        return state == ResultState.OK;
    }

    @Override
    public boolean isFail() {
        return state == ResultState.FAIL;
    }
}
