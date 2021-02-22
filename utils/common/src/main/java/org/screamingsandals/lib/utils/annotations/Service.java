package org.screamingsandals.lib.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Service annotation is used to match class which can be initialized by screaming-annotation processor.
 * This class requires to have static init method. If init method is not present, constructor will be used.
 * <p>
 * Init method or constructor can contain this arguments:
 * <ul>
 *  <li>PluginContainer</li>
 *  <li>Platform plugin - not recommended unless this is platform-specific service</li>
 *  <li>{@link org.screamingsandals.lib.utils.Controllable}</li>
 * </ul>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Service {
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

    /**
     * Defines if service can't be registered to ServiceManager.
     * If it's true and init method is not present, object won't be constructed.
     * If class is annotated with {@link lombok.experimental.UtilityClass} this is implicitly true.
     *
     * @return true if service can't be registered to ServiceManager
     */
    boolean staticOnly() default false;
}
