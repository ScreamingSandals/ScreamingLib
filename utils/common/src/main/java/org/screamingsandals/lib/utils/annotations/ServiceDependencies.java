package org.screamingsandals.lib.utils.annotations;

/**
 * An annotation for specifying service dependencies without using a {@link Service} annotation.
 * An example usage would be declaring service dependencies for abstract classes, which are yet to be subclassed.
 */
public @interface ServiceDependencies {
    /**
     * Defines services which are required by this service to run.
     * These services will be automatically initialized if the annotation module is declared as an annotation processor in your build system.
     *
     * @return the service dependencies
     */
    Class<?>[] dependsOn() default {};

    /**
     * Defines services which are not required by this service, but can be loaded optionally.
     * These services won't be loaded automatically if they are not declared as a required dependency anywhere else.
     *
     * @return the services
     */
    Class<?>[] loadAfter() default {};
}
