package org.screamingsandals.commands.velocity.guice;

import org.screamingsandals.commands.api.registry.CommandRegistry;
import org.screamingsandals.commands.api.registry.ServerRegistry;
import org.screamingsandals.commands.core.command.AbstractCommandWrapper;
import org.screamingsandals.commands.core.guice.AbstractCommandModule;
import org.screamingsandals.commands.velocity.registry.VelocityCommandRegistry;
import org.screamingsandals.commands.velocity.registry.VelocityServerRegistry;
import org.screamingsandals.commands.velocity.wrapper.VelocityCommandWrapper;

public class VelocityCommandModule extends AbstractCommandModule {

    @Override
    protected void configure() {
        super.configure();

        bind(ServerRegistry.class)
                .to(VelocityServerRegistry.class)
                .asEagerSingleton();
        bind(AbstractCommandWrapper.class)
                .to(VelocityCommandWrapper.class)
                .asEagerSingleton();
        bind(CommandRegistry.class)
                .to(VelocityCommandRegistry.class)
                .asEagerSingleton();
    }
}
