package org.screamingsandals.lib.utils.config;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.xml.XmlConfigurationLoader;

public class XmlConfigurationLoaderBuilderSupplier implements ConfigurationLoaderBuilderSupplier {
    public static final @NotNull XmlConfigurationLoaderBuilderSupplier INSTANCE = new XmlConfigurationLoaderBuilderSupplier();

    @Override
    public AbstractConfigurationLoader.@NotNull Builder<?,?> get() throws UnsupportedOperationException {
        // good relocation plugins should be able to handle these class strings
        if (Reflect.has("org.spongepowered.configurate.xml.XmlConfigurationLoader")) {
            return XmlConfigurationLoader.builder();
        } else {
            throw new UnsupportedOperationException("There is no Configurate Loader capable of loading xml files!");
        }
    }
}
