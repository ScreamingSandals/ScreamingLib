package org.screamingsandals.commands.core.guice;

import com.google.inject.AbstractModule;
import org.screamingsandals.commands.api.builder.SCBuilder;
import org.screamingsandals.commands.core.registry.AbstractCommandRegistry;

public abstract class AbstractCommandModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SCBuilder.class).asEagerSingleton();
        bind(AbstractCommandRegistry.class).asEagerSingleton();
    }
}
