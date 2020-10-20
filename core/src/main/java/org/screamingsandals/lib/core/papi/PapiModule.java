package org.screamingsandals.lib.core.papi;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.core.wrapper.PluginWrapper;

@RequiredArgsConstructor
public class PapiModule extends AbstractModule {
    private final PluginWrapper pluginWrapper;

    @Override
    protected void configure() {
        final var papi = new PlaceholderConfig();

        if (pluginWrapper.isPluginEnabled("PlaceholderAPI")) {
            papi.setEnabled(true);
            papi.setUsePapi(true);
        }

        bind(PlaceholderConfig.class).toInstance(papi);
    }
}
