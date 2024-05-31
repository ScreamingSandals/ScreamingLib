package org.screamingsandals.lib.impl.utils.config;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

@UtilityClass
public class YamlSupplier {
    public static AbstractConfigurationLoader.@NotNull Builder<?,?> obtainBuilder() {
        return YamlConfigurationLoader.builder();
    }
}
