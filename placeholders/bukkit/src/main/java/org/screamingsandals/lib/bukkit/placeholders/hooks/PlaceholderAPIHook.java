package org.screamingsandals.lib.bukkit.placeholders.hooks;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.placeholders.PlaceholderExpansion;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.utils.AdventureHelper;

@RequiredArgsConstructor
public class PlaceholderAPIHook implements Hook {
    private final Plugin plugin;

    @Override
    public void register(PlaceholderExpansion expansion) {
        new GenericExpansion(plugin, expansion).register();
    }

    @Override
    public String resolveString(MultiPlatformOfflinePlayer player, String message) {
        return PlaceholderAPI.setPlaceholders(player.as(OfflinePlayer.class), message);
    }

    @RequiredArgsConstructor
    public static class GenericExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {
        private final Plugin plugin;
        private final PlaceholderExpansion placeholderExpansion;

        @Override
        @NotNull
        public String getIdentifier() {
            return placeholderExpansion.getIdentifier();
        }

        @Override
        @NotNull
        public String getAuthor() {
            return String.join(", ", plugin.getDescription().getAuthors());
        }

        @Override
        @NotNull
        public String getVersion() {
            return plugin.getDescription().getVersion();
        }

        @Override
        public String onRequest(OfflinePlayer player, @NotNull String params) {
            return AdventureHelper.toLegacyNullableResult(placeholderExpansion.onRequest(PlayerMapper.wrapOfflinePlayer(player), params));
        }

        @Override
        public String onPlaceholderRequest(Player player, @NotNull String params) {
            // huh, older version?
            return onRequest(player, params);
        }

        @SuppressWarnings("all")
        public String getPlugin() {
            return plugin.getName();
        }
    }
}
