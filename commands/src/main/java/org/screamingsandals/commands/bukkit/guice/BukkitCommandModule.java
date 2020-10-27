package org.screamingsandals.commands.bukkit.guice;

import org.screamingsandals.commands.api.registry.CommandRegistry;
import org.screamingsandals.commands.api.registry.ServerRegistry;
import org.screamingsandals.commands.bukkit.registry.BukkitCommandRegistry;
import org.screamingsandals.commands.bukkit.registry.BukkitServerRegistry;
import org.screamingsandals.commands.bukkit.wrapper.BukkitCommandWrapper;
import org.screamingsandals.commands.core.command.AbstractCommandWrapper;
import org.screamingsandals.commands.core.guice.AbstractCommandModule;

public class BukkitCommandModule extends AbstractCommandModule {

    @Override
    protected void configure() {
        super.configure();

        bind(ServerRegistry.class)
                .to(BukkitServerRegistry.class)
                .asEagerSingleton();
        bind(AbstractCommandWrapper.class)
                .to(BukkitCommandWrapper.class)
                .asEagerSingleton();
        bind(CommandRegistry.class)
                .to(BukkitCommandRegistry.class)
                .asEagerSingleton();
    }
}
