package org.screamingsandals.lib.results;

import java.io.Serializable;

public interface BaseResult extends Serializable {

    Result.Status getStatus();

    String getMessage();

    Throwable getThrowable();

    boolean isOk();

    boolean isFail();

    default Result convert() {
        final var message = getMessage() == null ? "" : getMessage();

        Result.Status status;
        switch (getStatus()) {
            case OK:
                status = Result.Status.OK;
                break;
            case FAIL:
                status = Result.Status.FAIL;
                break;
            default:
                status = Result.Status.UNRECOGNIZED;
        }

        return Result
                .builder()
                .message(message)
                .status(status)
                .build();
    }
}

