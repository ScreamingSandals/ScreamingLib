package org.screamingsandals.lib.results;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder(access = AccessLevel.PACKAGE)
@Getter
@ToString
public class DataResultImpl<T> implements DataResult<T> {
    private final Result.Status status;
    private final String message;
    private final Throwable throwable;
    private final T data;

    @Override
    public boolean hasData() {
        return data != null;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K> Optional<K> getDataAs(Class<K> kClass) {
        try {
            return (Optional<K>) getData();
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isOk() {
        return status == Result.Status.OK;
    }

    @Override
    public boolean isFail() {
        return status == Result.Status.FAIL;
    }
}

