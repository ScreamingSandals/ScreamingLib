package org.screamingsandals.lib.core.papi;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.core.plugin.PluginCore;

@RequiredArgsConstructor
public class PapiModule extends AbstractModule {
    private final PluginCore pluginCore;

    @Override
    protected void configure() {
        final var papi = new PlaceholderConfig();

        if (pluginCore.isPluginEnabled("PlaceholderAPI")) {
            papi.setEnabled(true);
            papi.setUsePapi(true);
        }

        bind(PlaceholderConfig.class).toInstance(papi);
    }
}
