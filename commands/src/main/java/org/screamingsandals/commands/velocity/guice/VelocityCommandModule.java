package org.screamingsandals.commands.velocity.guice;

import org.screamingsandals.commands.core.guice.AbstractCommandModule;

public class VelocityCommandModule extends AbstractCommandModule {

    @Override
    protected void configure() {
        super.configure();

        //bind(VelocityCommandWrapper.class).asEagerSingleton();
    }
}
