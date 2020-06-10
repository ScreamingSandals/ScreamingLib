package org.screamingsandals.lib.gamecore.error;

import lombok.Data;
import org.screamingsandals.lib.gamecore.GameCore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.m;

@Data
public class BaseError implements Serializable {
    private final ErrorType errorType;
    private final Exception exception;
    private transient String languageKey;
    private transient String message;
    protected final transient Map<String, Object> placeholders;

    public BaseError(ErrorType errorType, Exception exception) {
        this.errorType = errorType;
        this.exception = exception;
        this.languageKey = errorType.getKey();
        this.placeholders = new HashMap<>();

        if (!m(languageKey).exists()) {
            this.message = GameCore.getErrorManager().getDefaultMessages().get(errorType);
        } else {
            this.message = m(languageKey).get();
        }
    }

    public BaseError addPlaceholder(String key, Object value) {
        placeholders.put(key, value);
        return this;
    }

    public String getMessage() {
        var toReturn = message;
        for (var entry : placeholders.entrySet()) {
            final var value = entry.getValue();
            var valueToUse = "null";

            if (value != null) {
                valueToUse = value.toString();
            }

            toReturn = toReturn.replaceAll(entry.getKey(), valueToUse);
        }
        return toReturn;
    }
}
