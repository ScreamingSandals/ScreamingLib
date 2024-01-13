/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.impl.bukkit.placeholders.hooks;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.placeholders.PlaceholderExpansion;
import org.screamingsandals.lib.placeholders.hooks.Hook;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;

@RequiredArgsConstructor
public class PlaceholderAPIHook implements Hook {
    private final @NotNull Plugin plugin;

    @Override
    public void register(@NotNull PlaceholderExpansion expansion) {
        new GenericExpansion(plugin, expansion).register();
    }

    @Override
    public @NotNull String resolveString(@Nullable MultiPlatformOfflinePlayer player, @NotNull String message) {
        return PlaceholderAPI.setPlaceholders(player == null ? null : player.as(OfflinePlayer.class), message);
    }

    @RequiredArgsConstructor
    public static class GenericExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {
        private final @NotNull Plugin plugin;
        private final @NotNull PlaceholderExpansion placeholderExpansion;

        @Override
        public @NotNull String getIdentifier() {
            return placeholderExpansion.getIdentifier();
        }

        @Override
        public @NotNull String getAuthor() {
            return String.join(", ", plugin.getDescription().getAuthors());
        }

        @Override
        public @NotNull String getVersion() {
            return plugin.getDescription().getVersion();
        }

        @Override
        public @Nullable String onRequest(@Nullable OfflinePlayer player, @NotNull String params) {
            var result = placeholderExpansion.onRequest(player == null ? null : Players.wrapOfflinePlayer(player), params);
            return result != null ? result.toLegacy() : null;
        }

        @Override
        public @Nullable String onPlaceholderRequest(@Nullable Player player, @NotNull String params) {
            // huh, older version?
            return onRequest(player, params);
        }

        @SuppressWarnings("all")
        public @NotNull String getPlugin() {
            return plugin.getName();
        }

        @Override
        public boolean persist() {
            return true;
        }
    }
}
