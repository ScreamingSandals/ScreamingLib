package org.screamingsandals.lib.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Plugin {
    String id();
    String version();
    String name() default "";
    String description() default "";
    String[] authors() default {};
    LoadTime loadTime() default LoadTime.POSTWORLD;

    enum LoadTime {
        STARTUP,
        POSTWORLD;
    }
}
