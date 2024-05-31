package org.screamingsandals.lib.impl.utils.config;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

@UtilityClass
public class JacksonSupplier {
    public static AbstractConfigurationLoader.@NotNull Builder<?,?> obtainBuilder() {
        return JacksonConfigurationLoader.builder();
    }
}
