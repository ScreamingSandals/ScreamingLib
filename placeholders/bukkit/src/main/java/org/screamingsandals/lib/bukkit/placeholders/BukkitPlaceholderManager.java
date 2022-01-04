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

package org.screamingsandals.lib.bukkit.placeholders;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.placeholders.hooks.Hook;
import org.screamingsandals.lib.bukkit.placeholders.hooks.PlaceholderAPIHook;
import org.screamingsandals.lib.bukkit.player.BukkitPlayerMapper;
import org.screamingsandals.lib.placeholders.PlaceholderExpansion;
import org.screamingsandals.lib.placeholders.PlaceholderManager;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.LinkedList;
import java.util.List;

@Service(dependsOn = {
        BukkitPlayerMapper.class
})
public class BukkitPlaceholderManager extends PlaceholderManager {

    private final List<Hook> activeHooks = new LinkedList<>();

    public static void init(Plugin plugin, Controllable controllable) {
        PlaceholderManager.init(() -> new BukkitPlaceholderManager(plugin, controllable));
    }

    public BukkitPlaceholderManager(Plugin plugin, Controllable controllable) {
        controllable
                .enable(() -> {
                    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                        activeHooks.add(new PlaceholderAPIHook(plugin));
                    }
                })
                .disable(activeHooks::clear);
    }

    @Override
    public void registerExpansion0(PlaceholderExpansion expansion) {
        activeHooks.forEach(hook -> hook.register(expansion));
    }

    @Override
    public String resolveString0(MultiPlatformOfflinePlayer player, String message) {
        for (var hook : activeHooks) {
            message = hook.resolveString(player, message);
        }
        return message;
    }
}
