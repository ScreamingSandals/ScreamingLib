package org.screamingsandals.lib.core;

import com.google.inject.AbstractModule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.core.lang.guice.LanguageModule;

@EqualsAndHashCode(callSuper = false)
@Data
public class ScreamingModule extends AbstractModule {
    private final PluginCore pluginCore;

    @Override
    protected void configure() {
        bind(PluginCore.class).toInstance(pluginCore);

        install(new LanguageModule());
    }
}
