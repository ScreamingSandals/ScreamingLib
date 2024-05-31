package org.screamingsandals.lib.impl.utils.config;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

@UtilityClass
public class GsonSupplier {
    public static AbstractConfigurationLoader.@NotNull Builder<?,?> obtainBuilder() {
        return GsonConfigurationLoader.builder();
    }
}
