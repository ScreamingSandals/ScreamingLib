package org.screamingsandals.lib.gamecore.error;

import lombok.Data;

import static org.screamingsandals.lib.lang.I.m;

@Data
public class BaseError {
    private final ErrorType errorType;
    private final Exception exception;
    private String languageKey;
    private String defaultMessage;
    private String message;

    public BaseError(ErrorType errorType, Exception exception) {
        this.errorType = errorType;
        this.exception = exception;
        this.languageKey = errorType.getLanguageKey();
        this.defaultMessage = errorType.getDefaultMessage();

        var message = m(languageKey).get();
        if (message.equalsIgnoreCase(languageKey)) {
            this.message = defaultMessage;
        }

        this.message = message;
    }
}
