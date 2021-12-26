package org.screamingsandals.lib.bukkit.plugin;

import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.utils.BasicWrapper;

public class BukkitPluginKey extends BasicWrapper<String> implements PluginKey {
    private BukkitPluginKey(String name) {
        super(name);
    }

    public static BukkitPluginKey of(String name) {
        return new BukkitPluginKey(name);
    }
}
