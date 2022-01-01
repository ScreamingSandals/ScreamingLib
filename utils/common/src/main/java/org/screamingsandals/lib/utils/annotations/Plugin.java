package org.screamingsandals.lib.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation specifying the main plugin class.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Plugin {
    /**
     * Defines a plugin identifier, should be your plugin's name.
     *
     * @return the plugin id
     */
    String id();

    /**
     * Defines the version of the plugin (0.0.1-SNAPSHOT for example).
     *
     * @return the plugin version
     */
    String version();
    String name() default "";
    String description() default "";
    String[] authors() default {};
    LoadTime loadTime() default LoadTime.POSTWORLD;

    enum LoadTime {
        STARTUP,
        POSTWORLD
    }
}
