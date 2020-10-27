package org.screamingsandals.commands.guice;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.commands.bukkit.guice.BukkitCommandModule;
import org.screamingsandals.commands.bungee.guice.BungeeCommandModule;
import org.screamingsandals.commands.core.auto.CommandClassScanner;
import org.screamingsandals.commands.velocity.guice.VelocityCommandModule;
import org.screamingsandals.lib.core.ScreamingModule;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class CommandModule extends AbstractModule {
    private final PluginWrapper pluginWrapper;

    @Override
    protected void configure() {
        install(new ScreamingModule(pluginWrapper));

        LoggerFactory.getLogger("test").warn("type: {}", pluginWrapper.getType());
        switch (pluginWrapper.getType()) {
            case BUKKIT:
                install(new BukkitCommandModule());
                break;
            case BUNGEE:
                install(new BungeeCommandModule());
                break;
            case VELOCITY:
                install(new VelocityCommandModule());
                break;
        }

        bind(CommandClassScanner.class).asEagerSingleton();
    }
}
