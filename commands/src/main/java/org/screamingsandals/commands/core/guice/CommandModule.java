package org.screamingsandals.commands.core.guice;

import com.google.inject.AbstractModule;
import org.screamingsandals.commands.api.builder.SCBuilder;

public class CommandModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SCBuilder.class).asEagerSingleton();
    }
}
