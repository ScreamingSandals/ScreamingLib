package org.screamingsandals.commands.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import org.screamingsandals.commands.api.builder.SCBuilder;
import org.screamingsandals.commands.core.handler.CommandHandlerImpl;
import org.screamingsandals.commands.core.handler.TabHandlerImpl;

public abstract class AbstractCommandModule extends AbstractModule {

    @Override
    protected void configure() {
        bindListener(Matchers.any(), new CommandClassListener());
        bind(SCBuilder.class).asEagerSingleton();

        bind(CommandHandlerImpl.class).asEagerSingleton();
        bind(TabHandlerImpl.class).asEagerSingleton();
    }
}
