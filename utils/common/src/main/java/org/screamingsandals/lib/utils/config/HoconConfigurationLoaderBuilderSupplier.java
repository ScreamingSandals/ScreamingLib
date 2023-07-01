package org.screamingsandals.lib.utils.config;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

public class HoconConfigurationLoaderBuilderSupplier implements ConfigurationLoaderBuilderSupplier {
    public static final @NotNull HoconConfigurationLoaderBuilderSupplier INSTANCE = new HoconConfigurationLoaderBuilderSupplier();

    @Override
    public AbstractConfigurationLoader.@NotNull Builder<?,?> get() throws UnsupportedOperationException {
        // good relocation plugins should be able to handle these class strings
        if (Reflect.has("org.spongepowered.configurate.hocon.HoconConfigurationLoader")) {
            return HoconConfigurationLoader.builder();
        } else {
            throw new UnsupportedOperationException("There is no Configurate Loader capable of loading xml files!");
        }
    }
}
