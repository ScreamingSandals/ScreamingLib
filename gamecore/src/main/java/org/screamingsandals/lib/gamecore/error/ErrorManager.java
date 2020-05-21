package org.screamingsandals.lib.gamecore.error;

import lombok.Data;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;

import java.util.LinkedList;
import java.util.List;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.m;

/**
 * Utility class for writing errors and logging them.
 * This will be used with our debug-paste tool
 */
@Data
public class ErrorManager {
    private final List<BaseError> errorLog = new LinkedList<>();

    public void destroy() {
        errorLog.clear();
    }

    public BaseError newError(BaseError entry) {
        return newError(entry, false);
    }

    public BaseError newError(BaseError entry, boolean writeError) {
        errorLog.add(entry);
        writeError(entry, writeError);

        return entry;
    }

    public void writeError(BaseError error, boolean stackTrace) {
        Debug.warn(error.getMessage(), true);

        if (stackTrace) {
            error.getException().printStackTrace();
        }

        if (GameCore.getInstance().isVerbose()) {
            GameCore.getPlayerManager().getRegisteredPlayers().forEach(gamePlayer -> {
                final var player = gamePlayer.getPlayer();
                if (player.hasPermission(GameCore.getInstance().getAdminPermissions())) {
                    player.sendMessage(m("prefix").get() + " " + error.getMessage());
                }
            });
        }
    }
}
