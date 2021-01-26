package org.screamingsandals.lib.plugin;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;


@Getter
public abstract class PluginContainer {
    private PluginDescription pluginDescription;
    private Logger logger;

    public void init(@NotNull PluginDescription pluginDescription, Logger logger) {
        if (this.pluginDescription != null) {
            throw new UnsupportedOperationException(pluginDescription.getName() + " is already initialized!");
        }
        this.pluginDescription = pluginDescription;
        this.logger = logger;
    }

    public void load() {
    }

    public void enable() {
    }

    public void disable() {
    }
}
