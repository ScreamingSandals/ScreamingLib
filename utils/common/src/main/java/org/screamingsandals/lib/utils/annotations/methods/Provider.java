package org.screamingsandals.lib.utils.annotations.methods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Provider annotation is used to mark method in {@link org.screamingsandals.lib.utils.annotations.Service}
 * which provides object that can be injected to another Service. Each type of object can have only one provider method.
 * Each provider is called only once in the specified level.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Provider {
    Level level() default Level.INIT;

    enum Level {
        INIT,
        ENABLE,
        POST_ENABLE;
    }
}
