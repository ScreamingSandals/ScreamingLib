package org.screamingsandals.lib.core.config.exception;

public class SConfigException extends Exception {

    public SConfigException(String message) {
        super(message);
    }

    public SConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
