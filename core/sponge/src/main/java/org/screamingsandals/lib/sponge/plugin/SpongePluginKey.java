package org.screamingsandals.lib.sponge.plugin;

import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.utils.BasicWrapper;

public class SpongePluginKey extends BasicWrapper<String> implements PluginKey {
    private SpongePluginKey(String name) {
        super(name);
    }

    public static SpongePluginKey of(String name) {
        return new SpongePluginKey(name);
    }
}
