package org.screamingsandals.commands.bungee.guice;

import org.screamingsandals.commands.api.registry.CommandRegistry;
import org.screamingsandals.commands.api.registry.ServerRegistry;
import org.screamingsandals.commands.bungee.registry.BungeeCommandRegistry;
import org.screamingsandals.commands.bungee.registry.BungeeServerRegistry;
import org.screamingsandals.commands.bungee.wrapper.BungeeCommandWrapper;
import org.screamingsandals.commands.core.command.AbstractCommandWrapper;
import org.screamingsandals.commands.core.guice.AbstractCommandModule;

public class BungeeCommandModule extends AbstractCommandModule {

    @Override
    protected void configure() {
        super.configure();

        bind(ServerRegistry.class)
                .to(BungeeServerRegistry.class)
                .asEagerSingleton();
        bind(AbstractCommandWrapper.class)
                .to(BungeeCommandWrapper.class)
                .asEagerSingleton();
        bind(CommandRegistry.class)
                .to(BungeeCommandRegistry.class)
                .asEagerSingleton();
    }
}
