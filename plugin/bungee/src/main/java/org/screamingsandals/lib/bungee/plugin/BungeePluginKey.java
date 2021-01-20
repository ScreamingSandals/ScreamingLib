package org.screamingsandals.lib.bungee.plugin;

import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.utils.BasicWrapper;

public class BungeePluginKey extends BasicWrapper<String> implements PluginKey {
    private BungeePluginKey(String name) {
        super(name);
    }

    public static BungeePluginKey of(String name) {
        return new BungeePluginKey(name);
    }
}
