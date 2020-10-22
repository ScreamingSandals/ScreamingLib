package org.screamingsandals.lib.core.util.result;

import java.util.Optional;

public interface Result {

    ResultState getState();

    String getMessage();

    Throwable getThrowable();

    <T> Optional<T> getData();

    <T> Optional<T> getData(Class<T> tClass);

    <T> Optional<T> getDataIfOk(Class<T> tClass);

    boolean isData();

    boolean isOk();

    boolean isFail();

    static Result ok() {
        return SimpleResult.builder()
                .state(ResultState.OK)
                .build();
    }

    static Result ok(String message) {
        return SimpleResult.builder()
                .state(ResultState.OK)
                .message(message)
                .build();
    }

    static Result ok(String message, Object data) {
        return SimpleResult.builder()
                .state(ResultState.OK)
                .message(message)
                .data(data)
                .build();
    }

    static Result fail(String message) {
        return SimpleResult.builder()
                .state(ResultState.FAIL)
                .message(message)
                .build();
    }

    static Result fail(String message, Throwable throwable) {
        return SimpleResult.builder()
                .state(ResultState.FAIL)
                .message(message)
                .throwable(throwable)
                .build();
    }

    static Result unknown() {
        return SimpleResult.builder()
                .state(ResultState.UNKNOWN)
                .build();
    }

    static Result unknown(String message) {
        return SimpleResult.builder()
                .state(ResultState.UNKNOWN)
                .message(message)
                .build();
    }
}
