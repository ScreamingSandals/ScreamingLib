package org.screamingsandals.lib.plugin;

import lombok.Getter;

@Getter
public abstract class PluginContainer {
    private PluginDescription pluginDescription;

    public void init(PluginDescription pluginDescription) {
        if (this.pluginDescription != null) {
            throw new UnsupportedOperationException(pluginDescription.getName() + " is already initialized!");
        }
        this.pluginDescription = pluginDescription;
    }

    public abstract void onLoad();

    public abstract void onEnable();

    public abstract void onDisable();
}
