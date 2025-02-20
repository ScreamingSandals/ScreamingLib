/*
 * Copyright 2025 ScreamingSandals
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A service factory is a class used to construct a specific instance of a {@link ProvidedService}.
 * It has a single method called {@code create} or annotated using, which returns the instance
 * of the specific service. The method can request the same parameters as a constructor of any other {@link Service}.
 * <p>
 * Following declarative annotations can be used on a ServiceFactory:
 * <ul>
 *     <li>{@link ServiceDependencies}</li>
 *     <li>{@link org.screamingsandals.lib.utils.annotations.internal.AccessPluginClasses}</li>
 * </ul>
 * Other annotations are ignored.
 * <p>
 * To register a ServiceFactory, simply depend on it from the plugin itself or any other service.
 * <p>
 * Note: The annotation processor does NOT check the code of the method, only the return type, thus only annotated methods
 * in the base type (and their overrides in specific implementations) are used. If you need to add a new controllable (preEnable, enable, etc.)
 * or event handler, you should request the {@link org.screamingsandals.lib.utils.Controllable} object or the event manager as a parameter.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ServiceFactory {
}
