package org.screamingsandals.lib.utils.config;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public class YamlConfigurationLoaderBuilderSupplier implements ConfigurationLoaderBuilderSupplier {
    public static final @NotNull YamlConfigurationLoaderBuilderSupplier INSTANCE = new YamlConfigurationLoaderBuilderSupplier();

    @Override
    public AbstractConfigurationLoader.@NotNull Builder<?,?> get() throws UnsupportedOperationException {
        // good relocation plugins should be able to handle these class strings
        if (Reflect.has("org.spongepowered.configurate.yaml.YamlConfigurationLoader")) {
            return YamlConfigurationLoader.builder();
        } else {
            throw new UnsupportedOperationException("There is no ConfigurateLoader capable of loading yaml files!");
        }
    }
}
