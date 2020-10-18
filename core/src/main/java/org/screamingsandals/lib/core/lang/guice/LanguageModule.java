package org.screamingsandals.lib.core.lang.guice;

import com.google.inject.AbstractModule;
import org.screamingsandals.lib.core.lang.registry.PlayerRegistry;
import org.screamingsandals.lib.core.lang.registry.FileRegistry;

public class LanguageModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FileRegistry.class).asEagerSingleton();
        bind(PlayerRegistry.class).asEagerSingleton();
    }
}
