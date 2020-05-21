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
            errorType.getReplaceable().putAll(gameFrame.getPlaceholderParser().getAvailable());
            errorType.getReplaceable().forEach((key, value) -> setMessage(getMessage().replaceAll(key, value.toString())));
        }
    }
}
