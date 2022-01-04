/*
 * Copyright 2022 ScreamingSandals
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
 * An annotation specifying the main plugin class.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Plugin {
    /**
     * Defines a plugin identifier, should be your plugin's name.
     *
     * @return the plugin id
     */
    String id();

    /**
     * Defines the version of the plugin (0.0.1-SNAPSHOT for example).
     *
     * @return the plugin version
     */
    String version();
    String name() default "";
    String description() default "";
    String[] authors() default {};
    LoadTime loadTime() default LoadTime.POSTWORLD;

    enum LoadTime {
        STARTUP,
        POSTWORLD
    }
}
