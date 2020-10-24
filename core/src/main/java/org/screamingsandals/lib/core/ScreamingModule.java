package org.screamingsandals.lib.core;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.core.lang.guice.LanguageModule;
import org.screamingsandals.lib.core.papi.PapiModule;
import org.screamingsandals.lib.core.tasker.guice.TaskerModule;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;
import org.screamingsandals.lib.core.wrapper.sender.BukkitWrapper;
import org.screamingsandals.lib.core.wrapper.sender.BungeeWrapper;

/**
 * Main ScreamingLib module.
 * This module contains everything needed in Core that can be initialized
 * via Guice.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ScreamingModule extends AbstractModule {
    private final PluginWrapper pluginWrapper;

    @Override
    protected void configure() {
        bind(PluginWrapper.class).toInstance(pluginWrapper);

        switch (pluginWrapper.getType()) {
            case BUKKIT:
                bind(org.bukkit.plugin.Plugin.class)
                        .annotatedWith(Names.named(pluginWrapper.getPluginName()))
                        .toInstance(pluginWrapper.getPlugin());
                bind(BukkitWrapper.class).asEagerSingleton();
                break;
            case BUNGEE:
                bind(net.md_5.bungee.api.plugin.Plugin.class)
                        .annotatedWith(Names.named(pluginWrapper.getPluginName()))
                        .toInstance(pluginWrapper.getPlugin());
                bind(BungeeWrapper.class).asEagerSingleton();
                break;
        }

        install(new TaskerModule(pluginWrapper));
        install(new PapiModule(pluginWrapper));
        install(new LanguageModule());
    }
}
