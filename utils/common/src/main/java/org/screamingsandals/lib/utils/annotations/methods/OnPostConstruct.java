package org.screamingsandals.lib.utils.annotations.methods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The OnPostConstruct annotation is used to mark method in {@link org.screamingsandals.lib.utils.annotations.Service}
 * which has to be called immediately after construction.
 * Each service can have only OnPostConstruct method.
 *
 * This annotation is mainly used for internal ScreamingLib services and may not have any utilization outside.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface OnPostConstruct {
}
