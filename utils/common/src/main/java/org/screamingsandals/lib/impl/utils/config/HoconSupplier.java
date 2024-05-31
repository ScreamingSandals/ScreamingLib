package org.screamingsandals.lib.impl.utils.config;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

@UtilityClass
public class HoconSupplier {
    public static AbstractConfigurationLoader.@NotNull Builder<?,?> obtainBuilder() {
        return HoconConfigurationLoader.builder();
    }
}
