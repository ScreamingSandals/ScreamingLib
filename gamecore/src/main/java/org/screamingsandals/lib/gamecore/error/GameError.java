package org.screamingsandals.lib.gamecore.error;

import lombok.Getter;
import org.screamingsandals.lib.gamecore.core.GameFrame;

public class GameError extends BaseError {
    @Getter
    private final GameFrame gameFrame;

    public GameError(GameFrame gameFrame, ErrorType errorType, Exception exception) {
        super(errorType, exception);
        this.gameFrame = gameFrame;

        if (gameFrame != null) {
            final var placeholderParser = gameFrame.getPlaceholderParser();
            if (placeholderParser != null) {
                errorType.getReplaceable().putAll(placeholderParser.getAvailable());
            }
            errorType.getReplaceable().forEach((key, value) -> {
                final var message = getMessage();
                if (message == null) {
                    return;
                }

                if (value == null) {
                    return;
                }

                setMessage(message.replaceAll(key, value.toString()));
            });
        }
    }
}
