package org.screamingsandals.lib.placeholders;

import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.function.Supplier;

@AbstractService
public abstract class PlaceholderManager {

    private static PlaceholderManager placeholderManager;

    public static void init(Supplier<PlaceholderManager> placeholderManagerSupplier) {
        if (placeholderManager != null) {
            throw new UnsupportedOperationException("PlaceholderManager is already initialized!");
        }
        placeholderManager = placeholderManagerSupplier.get();
    }

    public static void registerExpansion(PlaceholderExpansion expansion) {
        if (placeholderManager == null) {
            throw new UnsupportedOperationException("PlaceholderManager is not initialized yet!");
        }
        placeholderManager.registerExpansion0(expansion);
    }

    public abstract void registerExpansion0(PlaceholderExpansion expansion);

    public static String resolveString(MultiPlatformOfflinePlayer player, String message) {
        if (placeholderManager == null) {
            throw new UnsupportedOperationException("PlaceholderManager is not initialized yet!");
        }
        return placeholderManager.resolveString0(player, message);
    }

    public abstract String resolveString0(MultiPlatformOfflinePlayer player, String message);

    public static boolean isInitialized() {
        return placeholderManager != null;
    }
}
