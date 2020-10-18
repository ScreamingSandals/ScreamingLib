package org.screamingsandals.lib.core;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.core.lang.guice.LanguageModule;
import org.screamingsandals.lib.core.tasker.guice.TaskerModule;

@EqualsAndHashCode(callSuper = false)
@Data
public class ScreamingModule extends AbstractModule {
    private final PluginCore pluginCore;

    @Override
    protected void configure() {
        bind(PluginCore.class).toInstance(pluginCore);

        switch (pluginCore.getType()) {
            case PAPER:
                bind(org.bukkit.plugin.Plugin.class)
                        .annotatedWith(Names.named(pluginCore.getPluginName()))
                        .toInstance(pluginCore.getPlugin());
                break;
            case WATERFALL:
                bind(net.md_5.bungee.api.plugin.Plugin.class)
                        .annotatedWith(Names.named(pluginCore.getPluginName()))
                        .toInstance(pluginCore.getPlugin());
                break;
        }

        install(new TaskerModule(pluginCore));
        install(new LanguageModule());
    }
}
