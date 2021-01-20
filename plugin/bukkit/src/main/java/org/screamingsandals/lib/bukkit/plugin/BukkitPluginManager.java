package org.screamingsandals.lib.bukkit.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.plugin.PluginDescription;
import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.plugin.PluginManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BukkitPluginManager extends PluginManager {
    public static void init() {
        PluginManager.init(BukkitPluginManager::new);
    }

    @Override
    protected Object getPlatformClass0(PluginKey pluginKey) {
        return Bukkit.getPluginManager().getPlugin(pluginKey.as(String.class));
    }

    @Override
    protected boolean isEnabled0(PluginKey pluginKey) {
        return Bukkit.getPluginManager().isPluginEnabled(pluginKey.as(String.class));
    }

    @Override
    protected Optional<PluginDescription> getPlugin0(PluginKey pluginKey) {
        return Optional.ofNullable(Bukkit.getPluginManager().getPlugin(pluginKey.as(String.class))).map(this::wrap);
    }

    @Override
    protected List<PluginDescription> getAllPlugins0() {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(this::wrap).collect(Collectors.toList());
    }

    @Override
    protected Optional<PluginKey> createKey0(Object identifier) {
        return Optional.of(BukkitPluginKey.of(identifier.toString()));
    }

    private PluginDescription wrap(Plugin plugin) {
        return new PluginDescription(
                BukkitPluginKey.of(plugin.getName()),
                plugin.getName(),
                plugin.getDescription().getVersion(),
                plugin.getDescription().getDescription(),
                plugin.getDescription().getAuthors(),
                plugin.getDescription().getDepend(),
                plugin.getDescription().getSoftDepend(),
                plugin.getDataFolder().toPath().toAbsolutePath()
        );
    }
}
