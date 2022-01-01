package org.screamingsandals.lib.utils.annotations.methods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The ShouldRunControllable annotation is used to mark method in {@link org.screamingsandals.lib.utils.annotations.Service}
 * which returns boolean indicating whether or not controllable should be used.
 * Each service can have only one ShouldRunControllable method.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface ShouldRunControllable {
}
