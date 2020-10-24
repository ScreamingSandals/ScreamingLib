package org.screamingsandals.lib.core.tasker.guice;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;
import org.screamingsandals.lib.core.tasker.Tasker;
import org.screamingsandals.lib.core.tasker.TaskerWrapper;

/**
 * Module for initializing the {@link Tasker}
 */
@RequiredArgsConstructor
public class TaskerModule extends AbstractModule {
    private final PluginWrapper pluginWrapper;

    @Override
    protected void configure() {
        switch (pluginWrapper.getType()) {
            case BUKKIT:
                bind(Tasker.class).toInstance(new TaskerWrapper.BukkitTasker(pluginWrapper.getPlugin()));
                break;
            case BUNGEE:
                bind(Tasker.class).toInstance(new TaskerWrapper.BungeeTasker(pluginWrapper.getPlugin()));
                break;
        }
    }
}
