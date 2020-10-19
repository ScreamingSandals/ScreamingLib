package org.screamingsandals.lib.core.tasker.guice;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.core.plugin.PluginCore;
import org.screamingsandals.lib.core.tasker.Tasker;
import org.screamingsandals.lib.core.tasker.TaskerWrapper;

/**
 * Module for initializing the {@link Tasker}
 */
@RequiredArgsConstructor
public class TaskerModule extends AbstractModule {
    private final PluginCore pluginCore;

    @Override
    protected void configure() {
        switch (pluginCore.getType()) {
            case PAPER:
                bind(Tasker.class).toInstance(new TaskerWrapper.SpigotTasker(pluginCore.getPlugin()));
                break;
            case WATERFALL:
                bind(Tasker.class).toInstance(new TaskerWrapper.BungeeTasker(pluginCore.getPlugin()));
                break;
        }
    }
}
