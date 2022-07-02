package org.screamingsandals.lib.bukkit.placeholders.hooks;

import lombok.Data;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.screamingsandals.lib.placeholders.PlaceholderExpansion;
import org.screamingsandals.lib.placeholders.hooks.AbstractPAPILikePlaceholder;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;

// Vault placeholders support when PlaceholderAPI is not installed
// TODO: add remaining Vault placeholders: https://github.com/PlaceholderAPI/Vault-Expansion
@Data
public class VaultHook extends AbstractPAPILikePlaceholder {
    private final Chat chat;

    @Override
    public void register(PlaceholderExpansion expansion) {
        // you can't register placeholders to Vault ;)
    }

    @Override
    protected boolean has(String identifier) {
        return identifier.equals("vault_prefix") || identifier.equals("vault_suffix");
    }

    @Override
    protected String resolve(MultiPlatformOfflinePlayer player, String identifier, String parameters) {
        if (identifier.equals("vault_prefix")) {
            var prefix = chat.getPlayerPrefix(Bukkit.getWorlds().get(0).getName(), player.as(OfflinePlayer.class));
            return prefix == null ? "" : prefix;
        } else if (identifier.equals("vault_suffix")) {
            var prefix = chat.getPlayerSuffix(Bukkit.getWorlds().get(0).getName(), player.as(OfflinePlayer.class));
            return prefix == null ? "" : prefix;
        }
        return null;
    }
}
