package org.screamingsandals.commands.velocity.guice;

import com.google.inject.AbstractModule;
import org.screamingsandals.commands.velocity.wrapper.VelocityCommandWrapper;

public class VelocityCommandModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(VelocityCommandWrapper.class).asEagerSingleton();
    }
}
