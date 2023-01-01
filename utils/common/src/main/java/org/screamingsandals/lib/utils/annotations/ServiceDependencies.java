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
