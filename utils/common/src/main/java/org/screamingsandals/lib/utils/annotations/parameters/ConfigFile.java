package org.screamingsandals.lib.utils.annotations.parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The ConfigFile annotation is used to mark parameter in constructor or init method of {@link org.screamingsandals.lib.utils.annotations.Service}
 * which can be assigned to path of file in plugin's data folder. The parameter type must be {@link java.nio.file.Path}
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface ConfigFile {
    /**
     * Returns path of the file
     *
     * @return the path of the file
     */
    String value();

    /**
     * It's possible to move file from old location to new one via this annotation.
     *
     * @return the old path of the file
     */
    String old() default "";

    /**
     * If the annotated field is ConfigurationLoader, should Adventure serializers be included?
     *
     * @return should adventure serializers be included
     */
    boolean adventureSerializers() default true;
}
