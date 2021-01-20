package org.screamingsandals.lib.plugin;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@Getter
public abstract class PluginContainer {
    private PluginDescription pluginDescription;
    private Logger logger;

    public void init(@NotNull PluginDescription pluginDescription, @NotNull Logger logger) {
        if (this.pluginDescription != null) {
            throw new UnsupportedOperationException(pluginDescription.getName() + " is already initialized!");
        }
        this.pluginDescription = pluginDescription;
        this.logger = logger;
    }

    public abstract void onLoad();

    public abstract void onEnable();

    public abstract void onDisable();
}
