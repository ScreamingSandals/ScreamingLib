/*
 * Copyright 2023 ScreamingSandals
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

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for specifying service dependencies for a service and can be used on its superclasses as well.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ServiceDependencies {
    /**
     * Defines services which are required by this service to run.
     * These services will be automatically initialized if the annotation module is declared as an annotation processor in your build system.
     *
     * @return the service dependencies
     */
    @NotNull Class<?> @NotNull [] dependsOn() default {};

    /**
     * Defines services which are not required by this service, but can be loaded optionally.
     * These services won't be loaded automatically if they are not declared as a required dependency anywhere else.
     *
     * @return the services
     */
    @NotNull Class<?> @NotNull [] loadAfter() default {};

    /**
     * Defines services which should also be initialized. Equivalent to {@link Init} (you can't use Init for services yet)
     * These services will be automatically initialized if the annotation module is declared as an annotation processor in your build system.
     *
     * @return the services, which should be initialized independently of this service
     */
    @NotNull Class<?> @NotNull [] initAnother() default {};
}
