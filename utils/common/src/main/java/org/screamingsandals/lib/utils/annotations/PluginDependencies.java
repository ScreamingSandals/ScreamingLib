package org.screamingsandals.lib.utils.annotations;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.PlatformType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Repeatable(PluginDependencies.List.class)
public @interface PluginDependencies {
    PlatformType platform();
    String[] dependencies() default {};
    String[] softDependencies() default {};
    String[] loadBefore() default {};

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    @ApiStatus.Internal
    @interface List {
        PluginDependencies[] value();
    }
}
