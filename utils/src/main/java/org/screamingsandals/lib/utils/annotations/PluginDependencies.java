package org.screamingsandals.lib.utils.annotations;

import org.screamingsandals.lib.utils.PlatformType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface PluginDependencies {
    PlatformType platform();
    String[] dependencies() default {};
    String[] softDependencies() default {};
    String[] loadBefore() default {};
}
