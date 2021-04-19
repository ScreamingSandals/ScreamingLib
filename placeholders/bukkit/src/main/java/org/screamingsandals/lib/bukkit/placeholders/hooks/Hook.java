package org.screamingsandals.lib.bukkit.placeholders.hooks;

import org.screamingsandals.lib.placeholders.PlaceholderExpansion;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;

public interface Hook {
    void register(PlaceholderExpansion expansion);

    String resolveString(MultiPlatformOfflinePlayer player, String message);
}
