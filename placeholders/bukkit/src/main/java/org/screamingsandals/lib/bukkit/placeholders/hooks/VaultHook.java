/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.placeholders.hooks;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.screamingsandals.lib.placeholders.PlaceholderExpansion;
import org.screamingsandals.lib.placeholders.hooks.AbstractPAPILikePlaceholder;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;

// Vault placeholders support when PlaceholderAPI is not installed
// TODO: add remaining Vault placeholders: https://github.com/PlaceholderAPI/Vault-Expansion
@Data
@EqualsAndHashCode(callSuper = false)
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
