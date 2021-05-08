package org.screamingsandals.lib.results;


public interface SimpleResult extends BaseResult {

    static SimpleResult ok() {
        return SimpleResultImpl.builder()
                .status(Result.Status.OK)
                .build();
    }

    static SimpleResult ok(String message) {
        return SimpleResultImpl.builder()
                .status(Result.Status.OK)
                .message(message)
                .build();
    }

    static SimpleResult fail(String message) {
        return SimpleResultImpl.builder()
                .status(Result.Status.FAIL)
                .message(message)
                .build();
    }

    static SimpleResult fail(String message, Throwable throwable) {
        return SimpleResultImpl.builder()
                .status(Result.Status.FAIL)
                .message(message)
                .throwable(throwable)
                .build();
    }

    static SimpleResult fail(Throwable throwable) {
        return SimpleResultImpl.builder()
                .status(Result.Status.FAIL)
                .message(throwable.getMessage())
                .throwable(throwable)
                .build();
    }

    static SimpleResult unknown() {
        return SimpleResultImpl.builder()
                .status(Result.Status.UNKNOWN)
                .build();
    }

    static SimpleResult unknown(String message) {
        return SimpleResultImpl.builder()
                .status(Result.Status.UNKNOWN)
                .message(message)
                .build();
    }

    static SimpleResult convert(Result result) {
        return SimpleResultImpl.builder()
                .status(result.getStatus())
                .message(result.getMessage())
                .build();
    }

    default <T> DataResult<T> toData() {
        switch (this.getStatus()) {
            case FAIL:
                return DataResult.fail(getMessage());
            case OK:
                return DataResult.ok();
            default:
                return DataResult.unknown(getMessage());
        }
    }
}

