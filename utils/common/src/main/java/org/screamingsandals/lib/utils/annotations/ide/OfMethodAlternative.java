package org.screamingsandals.lib.utils.annotations.ide;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface OfMethodAlternative {
    Class<?> value();

    String methodName() default "of";
}
