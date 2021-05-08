package org.screamingsandals.lib.results;

import java.util.Optional;
import java.util.function.Consumer;

public interface DataResult<T> extends BaseResult {

    boolean hasData();

    T getData();

    <K> Optional<K> getDataAs(Class<K> kClass);

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> ok() {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Result.Status.OK)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> okData(T data) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Result.Status.OK)
                .data(data)
                .build();
    }

    static <T> DataResult<T> failIfNull(T data) {
        if (data == null) {
            return DataResult.fail("Data not found!");
        }

        return DataResult.okData(data);
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> ok(String message, T data) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Result.Status.OK)
                .message(message)
                .data(data)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> fail(String message) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Result.Status.FAIL)
                .message(message)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> fail(String message, Throwable throwable) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Result.Status.FAIL)
                .message(message)
                .throwable(throwable)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> fail(Throwable throwable) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Result.Status.FAIL)
                .message(throwable.getMessage())
                .throwable(throwable)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> unknown() {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Result.Status.UNKNOWN)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> unknown(String message) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(Result.Status.UNKNOWN)
                .message(message)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> convert(Result result) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(result.getStatus())
                .message(result.getMessage())
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> convert(Result result, T data) {
        return (DataResult<T>) DataResultImpl.builder()
                .status(result.getStatus())
                .message(result.getMessage())
                .data(data)
                .build();
    }

    @SuppressWarnings("unchecked")
    static <T> DataResult<T> transform(DataResult<?> dataResult) {
        return (DataResult<T>) dataResult;
    }

    default void ifOk(Consumer<T> data) {
        if (isOk() && hasData()) {
            data.accept(getData());
        }
    }

    default void ifOkOrElse(Consumer<T> data, Consumer<BaseResult> fallback) {
        if (isOk() && hasData()) {
            data.accept(getData());
            return;
        }

        fallback.accept(this);
    }

    default SimpleResult toSimple() {
        switch (this.getStatus()) {
            case FAIL:
                return SimpleResult.fail(getMessage());
            case OK:
                return SimpleResult.ok(getMessage());
            default:
                return SimpleResult.unknown(getMessage());
        }
    }
}
