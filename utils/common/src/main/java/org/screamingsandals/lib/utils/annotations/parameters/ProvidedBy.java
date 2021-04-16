package org.screamingsandals.lib.utils.annotations.parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The ProvidedBy annotation is used to mark parameter in constructor, init method or controllable methods of {@link org.screamingsandals.lib.utils.annotations.Service}
 * which can be assigned to object provided by {@link org.screamingsandals.lib.utils.annotations.methods.Provider}
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface ProvidedBy {
    Class<?> value();
}
