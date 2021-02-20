package org.screamingsandals.lib.utils.annotations.methods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The OnDisable annotation is used to match method in {@link org.screamingsandals.lib.utils.annotations.Service}
 * or {@link org.screamingsandals.lib.utils.annotations.AbstractService} which can be bound into Controllable.
 * Each service can have only OnDisable method.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface OnDisable {
}
