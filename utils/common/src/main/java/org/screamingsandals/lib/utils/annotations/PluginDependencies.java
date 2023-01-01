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

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.PlatformType;

import java.lang.annotation.*;

/**
 * An annotation for declaring other plugins as dependencies.
 * The class annotated with this annotation must extend <strong>org.screamingsandals.lib.plugin.PluginContainer</strong>.
 * This annotation is repeatable.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@Repeatable(PluginDependencies.List.class)
public @interface PluginDependencies {
    /**
     * Defines the platform on which the declared dependencies will take effect.
     *
     * @return the platform type
     */
    PlatformType platform();

    /**
     * Defines hard plugin dependencies (plugin won't load without them) for the annotated plugin class.
     *
     * @return the dependencies
     */
    String[] dependencies() default {};

    /**
     * Defines soft plugin dependencies (plugin will load without them, but tries to load them before itself, if they are present) for the annotated plugin class.
     *
     * @return the soft dependencies
     */
    String[] softDependencies() default {};

    /**
     * Defines plugins which will load <strong>after</strong> the plugin with the annotated plugin class.
     *
     * @return the load-before plugins
     */
    String[] loadBefore() default {};

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.TYPE)
    @ApiStatus.Internal
    @interface List {
        PluginDependencies[] value();
    }
}
