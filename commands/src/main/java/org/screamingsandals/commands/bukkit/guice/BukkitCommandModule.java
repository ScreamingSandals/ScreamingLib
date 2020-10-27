package org.screamingsandals.commands.bukkit.guice;

import org.screamingsandals.commands.bukkit.registry.BukkitCommandRegistry;
import org.screamingsandals.commands.bukkit.registry.BukkitServerRegistry;
import org.screamingsandals.commands.bukkit.wrapper.BukkitCommandWrapper;
import org.screamingsandals.commands.core.guice.AbstractCommandModule;

public class BukkitCommandModule extends AbstractCommandModule {

    @Override
    protected void configure() {
       super.configure();

        bind(BukkitCommandRegistry.class).asEagerSingleton();
        bind(BukkitServerRegistry.class).asEagerSingleton();
        bind(BukkitCommandWrapper.class).asEagerSingleton();
    }
}
