/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.utils.annotations;

import org.screamingsandals.lib.impl.utils.ControllableImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for automatically initializing the annotated class on plugin load.
 * The annotation module needs to be declared as an annotation processor in your build system for services to function.
 * <p>
 * Services employ a concept of "autowiring", a form of constructor/method dependency injection.
 * You can have the annotation processor automatically inject an object into your service's constructor or a method marked with @OnPostConstruct/@On[Post]Enable/@On[Pre]Disable/@OnPluginLoad.
 * <p>
 * Above-mentioned injectable objects include:
 * <ul>
 *  <li>a <strong>org.screamingsandals.lib.plugin.PluginContainer</strong> of your plugin if your plugin still uses it</li>
 *  <li>a <strong>org.screamingsandals.lib.plugin.Plugin</strong> of your plugin, which is a class describing the plugin</li>
 *  <li>the platform class of your plugin - not recommended, unless this is platform specific service</li>
 *  <li>
 *      a {@link org.screamingsandals.lib.utils.Controllable} or {@link ControllableImpl},
 *      yet we highly discourage from using controllables in favor of annotations from {@link org.screamingsandals.lib.utils.annotations}
 *  </li>
 *  <li>an another service defined in {@link ServiceDependencies#dependsOn()} or {@link ServiceDependencies#dependsOnConditioned()}</li>
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
 * <p>
 * Services required by constructor or lombok generated constructor don't need to be specified here unless they are conditioned.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Service {
    /**
     * Defines if this service should be registered to a <strong>org.screamingsandals.lib.plugin.ServiceManager</strong>.
     * If set to true, an object won't be constructed.
     * If the class is annotated with {@link lombok.experimental.UtilityClass}, this is implicitly true.
     *
     * @return should the service be registered to a ServiceManager?
     */
    boolean staticOnly() default false;
}
