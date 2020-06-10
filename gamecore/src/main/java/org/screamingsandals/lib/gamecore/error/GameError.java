package org.screamingsandals.lib.gamecore.error;

import lombok.Getter;
import org.screamingsandals.lib.gamecore.core.GameFrame;

public class GameError extends BaseError {
    @Getter
    private final transient GameFrame gameFrame;

    public GameError(GameFrame gameFrame, ErrorType errorType, Exception exception) {
        super(errorType, exception);
        this.gameFrame = gameFrame;

        if (gameFrame != null && gameFrame.getPlaceholderParser() != null) {
            placeholders.putAll(gameFrame.getPlaceholderParser().getAvailable());
        }
    }
}
