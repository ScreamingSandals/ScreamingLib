package org.screamingsandals.lib.minestom.plugin;

import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.utils.BasicWrapper;

public class MinestomPluginKey extends BasicWrapper<String> implements PluginKey {
    private MinestomPluginKey(String name) {
        super(name);
    }

    public static MinestomPluginKey of(String name) {
        return new MinestomPluginKey(name);
    }
}
