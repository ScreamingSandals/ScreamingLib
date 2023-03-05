/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.placeholders;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.placeholders.hooks.PlaceholderAPIHook;
import org.screamingsandals.lib.bukkit.placeholders.hooks.VaultHook;
import org.screamingsandals.lib.bukkit.player.BukkitPlayers;
import org.screamingsandals.lib.placeholders.PlaceholderExpansion;
import org.screamingsandals.lib.placeholders.PlaceholderManager;
import org.screamingsandals.lib.placeholders.hooks.DummyHook;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

@Service(dependsOn = BukkitPlayers.class)
public class BukkitPlaceholderManager extends PlaceholderManager {

    @ApiStatus.Internal
    @OnEnable
    public void onEnable(@NotNull Plugin plugin) {
        activeHooks.add(new DummyHook());
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            activeHooks.add(new PlaceholderAPIHook(plugin));
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            var chatProvider = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
            Chat vaultChat;
            if (chatProvider != null) {
                vaultChat = chatProvider.getProvider();
            } else {
                vaultChat = Bukkit.getServer().getServicesManager().load(Chat.class);
            }
            if (vaultChat != null) {
                activeHooks.add(new VaultHook(vaultChat));
            }
        }
    }

    @OnDisable
    public void onDisable() {
        activeHooks.clear();
    }

    @Override
    public void registerExpansion0(@NotNull PlaceholderExpansion expansion) {
        activeHooks.forEach(hook -> hook.register(expansion));
    }

    @Override
    public @NotNull String resolveString0(@Nullable MultiPlatformOfflinePlayer player, @NotNull String message) {
        for (var hook : activeHooks) {
            message = hook.resolveString(player, message);
        }
        return message;
    }
}
