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
            errorType.replacePlaceholders(gameFrame);
            System.out.println(errorType.getReplaceable());
            errorType.getReplaceable().forEach((key, value) -> setMessage(getMessage().replace(key, value)));
        }
    }
}
