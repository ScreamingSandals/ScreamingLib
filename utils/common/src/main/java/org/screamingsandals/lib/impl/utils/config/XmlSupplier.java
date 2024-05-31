package org.screamingsandals.lib.impl.utils.config;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.xml.XmlConfigurationLoader;

@UtilityClass
public class XmlSupplier {
    public static AbstractConfigurationLoader.@NotNull Builder<?,?> obtainBuilder() {
        return XmlConfigurationLoader.builder();
    }
}
