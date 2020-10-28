package org.screamingsandals.lib.core.tasker.guice;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.core.tasker.type.BukkitTasker;
import org.screamingsandals.lib.core.tasker.type.BungeeTasker;
import org.screamingsandals.lib.core.tasker.type.VelocityTasker;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;
import org.screamingsandals.lib.core.tasker.Tasker;

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
                bind(Tasker.class).to(BukkitTasker.class).asEagerSingleton();
                break;
            case BUNGEE:
                bind(Tasker.class).to(BungeeTasker.class).asEagerSingleton();
                break;
            case VELOCITY:
                bind(Tasker.class).to(VelocityTasker.class).asEagerSingleton();
            default:
                throw new UnsupportedOperationException("Unsupported plugin type!");
        }
    }
}
