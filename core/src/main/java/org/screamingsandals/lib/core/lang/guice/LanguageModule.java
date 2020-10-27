package org.screamingsandals.lib.core.lang.guice;

import com.google.inject.AbstractModule;
import org.screamingsandals.lib.core.lang.LanguageBase;
import org.screamingsandals.lib.core.lang.registry.FileRegistry;
import org.screamingsandals.lib.core.lang.registry.LanguageRegistry;
import org.screamingsandals.lib.core.lang.registry.PlayerRegistry;

public class LanguageModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LanguageBase.class).asEagerSingleton();
        bind(LanguageRegistry.class).asEagerSingleton();
        bind(PlayerRegistry.class).asEagerSingleton();
        bind(FileRegistry.class).asEagerSingleton();
    }
}
