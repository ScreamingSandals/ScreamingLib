package org.screamingsandals.lib.utils.annotations.parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The DataFolder annotation is used to mark parameter in constructor or init method of {@link org.screamingsandals.lib.utils.annotations.Service}
 * which can be assigned to DataFolder. The parameter type must be {@link java.nio.file.Path}
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface DataFolder {
    /**
     * Returns child path of the data folder. if child path is not empty string, annotation processor resolves the child path.
     *
     * @return the child path
     */
    String value() default "";
}
