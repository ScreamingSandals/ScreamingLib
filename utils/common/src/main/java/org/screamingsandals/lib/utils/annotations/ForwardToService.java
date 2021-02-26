package org.screamingsandals.lib.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ForwardToService {
    /**
     * Contains service or abstract service which should be initialized instead of this.
     *
     * @return class annotated with service or abstract service
     */
    Class<?> value();
}
