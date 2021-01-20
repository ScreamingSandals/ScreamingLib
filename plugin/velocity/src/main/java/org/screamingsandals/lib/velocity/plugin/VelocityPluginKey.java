package org.screamingsandals.lib.velocity.plugin;

import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.utils.BasicWrapper;

public class VelocityPluginKey extends BasicWrapper<String> implements PluginKey {
    private VelocityPluginKey(String name) {
        super(name);
    }

    public static VelocityPluginKey of(String name) {
        return new VelocityPluginKey(name);
    }
}
