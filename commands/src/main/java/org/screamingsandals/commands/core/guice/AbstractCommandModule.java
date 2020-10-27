package org.screamingsandals.commands.core.guice;

import com.google.inject.AbstractModule;
import org.screamingsandals.commands.api.builder.SCBuilder;
import org.screamingsandals.commands.api.registry.HandlerRegistry;
import org.screamingsandals.commands.core.registry.SimpleHandlerRegistry;

public abstract class AbstractCommandModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SCBuilder.class).asEagerSingleton();

        bind(HandlerRegistry.class)
                .to(SimpleHandlerRegistry.class)
                .asEagerSingleton();
    }
}
