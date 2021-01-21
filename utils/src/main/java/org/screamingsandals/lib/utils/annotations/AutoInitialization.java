package org.screamingsandals.lib.utils.annotations;

import org.screamingsandals.lib.utils.PlatformType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoInitialization {
    PlatformType platform();

    Class<?>[] loadAfter() default {};
}
