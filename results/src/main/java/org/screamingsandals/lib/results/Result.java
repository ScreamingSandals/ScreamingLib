package org.screamingsandals.lib.results;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
public class Result {
    private final Status status;
    private final String message;

    @RequiredArgsConstructor
    enum Status {
        FAIL(0),
        OK(1),
        UNKNOWN(2),
        UNRECOGNIZED(3);

        private final int status;
    }


}
