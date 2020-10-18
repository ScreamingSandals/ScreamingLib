package org.screamingsandals.lib.core.tasker.guice;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.core.PluginCore;
import org.screamingsandals.lib.core.tasker.Tasker;

@RequiredArgsConstructor
public class TaskerModule extends AbstractModule {
    private final PluginCore pluginCore;

    @Override
    protected void configure() {
        switch (pluginCore.getType()) {
            case PAPER:
                bind(Tasker.class).toInstance(Tasker.getSpigot(pluginCore.getPlugin()));
                break;
            case WATERFALL:
                bind(Tasker.class).toInstance(Tasker.getBungee(pluginCore.getPlugin()));
                break;
        }
    }
}
