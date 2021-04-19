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
