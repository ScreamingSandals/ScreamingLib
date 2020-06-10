package org.screamingsandals.lib.gamecore.error;

import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.bukkit.Bukkit;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.data.JsonUtils;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.m;

/**
 * Utility class for writing errors and logging them.
 * This will be used with our debug-paste tool
 */
@Data
public class ErrorManager {
    private Map<ErrorType, String> defaultMessages = new HashMap<>();
    private List<BaseError> errorLog = new LinkedList<>();

    public ErrorManager() {
        final var type = new TypeToken<Map<ErrorType, String>>() {
        }.getType();
        final var inputStream = getClass().getResourceAsStream("/core_errors.json");

        if (inputStream == null) {
            Debug.warn("Cannot load error core_errors.json!", true);
            return;
        }

        try (Reader reader = new InputStreamReader(inputStream)) {
            defaultMessages = JsonUtils.deserialize(reader, type);
        } catch (IOException e) {
            Debug.warn("Some error occurred while core_errors.json parsing data!", true);
            e.printStackTrace();
        }
    }

    public void destroy() {
        if (!errorLog.isEmpty()) {
            errorLog.clear();
        }
    }

    public BaseError newError(BaseError entry) {
        return newError(entry, false);
    }

    public BaseError newError(BaseError entry, boolean writeError) {
        if (entry == null) {
            return null;
        }

        errorLog.add(entry);
        writeError(entry, writeError);

        return entry;
    }

    public void writeError(BaseError error, boolean writeError) {
        if (!writeError) {
            return;
        }

        final var message = error.getMessage();

        Debug.warn(message, true);
        final var exception = error.getException();

        if (exception != null) {
            exception.printStackTrace();
        }

        if (GameCore.getInstance().isVerbose()) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.hasPermission(GameCore.getInstance().getAdminPermissions())) {
                    player.sendMessage(m("prefix").get() + " " + message);
                }
            });
        }
    }
}
