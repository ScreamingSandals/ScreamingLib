package org.screamingsandals.lib.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for automatically initializing the annotated class on plugin load.
 * The annotation module needs to be declared as an annotation processor in your build system for services to function.
 * <p>
 * Services employ a concept of "autowiring", a form of constructor/method dependency injection.
 * You can have the annotation processor automatically inject an object into your service's constructor or a method marked with @OnPostConstruct/@On[Post]Enable/@On[Pre]Disable.
 * <p>
 * Above-mentioned injectable objects include:
 * <ul>
 *  <li>a <strong>org.screamingsandals.lib.plugin.PluginContainer</strong> of your plugin</li>
 *  <li>a <strong>org.screamingsandals.lib.plugin.PluginDescription</strong> of your plugin</li>
 *  <li>the platform class of your plugin - not recommended, unless this is platform specific service</li>
 *  <li>a {@link org.screamingsandals.lib.utils.Controllable}</li>
 *  <li>an another service defined in {@link Service#dependsOn()}</li>
 *  <li>a {@link org.screamingsandals.lib.utils.logger.LoggerWrapper} or a SLF4J logger, if supported</li>
 *  <li>
 *      a {@link java.nio.file.Path} or a {@link java.io.File} annotated with
 *      {@link org.screamingsandals.lib.utils.annotations.parameters.ConfigFile} or {@link org.screamingsandals.lib.utils.annotations.parameters.DataFolder}
 *  </li>
 *  <li>
 *      any subclass of {@link org.spongepowered.configurate.loader.ConfigurationLoader} annotated with
 *      {@link org.screamingsandals.lib.utils.annotations.parameters.ConfigFile} or {@link org.screamingsandals.lib.utils.annotations.parameters.DataFolder}
 *  </li>
 * </ul>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Service {
    /**
     * Defines services which are required by this service to run.
     * These services will be automatically initialized if the annotation module is declared as an annotation processor in your build system.
     *
     * @return the service dependencies
     */
    Class<?>[] dependsOn() default {};

    /**
     * Defines services which are not required by this service, but can be loaded optionally.
     * These services won't be loaded automatically if they are not specified as a required dependency anywhere else.
     *
     * @return the services, which should be loaded optionally
     */
    Class<?>[] loadAfter() default {};

    /**
     * Defines services which should also be initialized. Equivalent to {@link Init} (you can't use Init for services yet)
     * These services will be automatically initialized if the annotation module is declared as an annotation processor in your build system.
     *
     * @return the services, which should be initialized independently of this service
     */
    Class<?>[] initAnother() default {};

    /**
     * Defines if this service should be registered to a <strong>org.screamingsandals.lib.plugin.ServiceManager</strong>.
     * If set to true, an object won't be constructed.
     * If the class is annotated with {@link lombok.experimental.UtilityClass}, this is implicitly true.
     *
     * @return should the service be registered to a ServiceManager?
     */
    boolean staticOnly() default false;
}
