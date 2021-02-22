package org.screamingsandals.lib.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The OnEvent annotation is used to match method in {@link org.screamingsandals.lib.utils.annotations.Service}
 * which can be registered to default EventManager.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface OnEvent {
    EventPriority priority() default EventPriority.NORMAL;

    boolean ignoreCancelled() default false;
}
