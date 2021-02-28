package org.screamingsandals.lib.utils.annotations;

/**
 * The ServiceDependencies annotation is used to specify service dependencies without using Service annotation.
 * This is used when the class is abstract.
 */
public @interface ServiceDependencies {
    /**
     * Defines all services which are required by this service to run.
     * These services will be automatically initialized if using screaming-annotation processor.
     *
     * @return All dependencies
     */
    Class<?>[] dependsOn() default {};

    /**
     * Defines all services which are not required by this service, but can be optionally used.
     * These services won't be loaded automatically if they are not specified as required by another service or PluginContainer.
     *
     * @return All services, that should be loaded before this service
     */
    Class<?>[] loadAfter() default {};
}
