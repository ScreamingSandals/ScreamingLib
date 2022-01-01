package org.screamingsandals.lib.utils.annotations;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.PlatformType;

import java.lang.annotation.*;

/**
 * An annotation for declaring other plugins as dependencies.
 * The class annotated with this annotation must extend <strong>org.screamingsandals.lib.plugin.PluginContainer</strong>.
 * This annotation is repeatable.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Repeatable(PluginDependencies.List.class)
public @interface PluginDependencies {
    /**
     * Defines the platform on which the declared dependencies will take effect.
     *
     * @return the platform type
     */
    PlatformType platform();

    /**
     * Defines hard plugin dependencies (plugin won't load without them) for the annotated plugin class.
     *
     * @return the dependencies
     */
    String[] dependencies() default {};

    /**
     * Defines soft plugin dependencies (plugin will load without them, but tries to load them before itself, if they are present) for the annotated plugin class.
     *
     * @return the soft dependencies
     */
    String[] softDependencies() default {};

    /**
     * Defines plugins which will load <strong>after</strong> the plugin with the annotated plugin class.
     *
     * @return the load-before plugins
     */
    String[] loadBefore() default {};

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    @ApiStatus.Internal
    @interface List {
        PluginDependencies[] value();
    }
}
