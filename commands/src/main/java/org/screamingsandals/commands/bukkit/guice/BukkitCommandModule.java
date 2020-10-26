package org.screamingsandals.commands.bukkit.guice;

import org.screamingsandals.commands.bukkit.registry.BukkitCommandRegistry;
import org.screamingsandals.commands.bukkit.registry.BukkitServerCommandRegistry;
import org.screamingsandals.commands.bukkit.wrapper.BukkitCommandWrapper;
import org.screamingsandals.commands.core.guice.AbstractCommandModule;

public class BukkitCommandModule extends AbstractCommandModule {

    @Override
    protected void configure() {
        super.configure();

        bind(BukkitCommandRegistry.class).asEagerSingleton();
        bind(BukkitServerCommandRegistry.class).asEagerSingleton();
        bind(BukkitCommandWrapper.class).asEagerSingleton();
    }
}
