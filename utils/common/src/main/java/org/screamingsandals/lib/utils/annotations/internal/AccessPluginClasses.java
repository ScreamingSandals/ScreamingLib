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

package org.screamingsandals.lib.utils.annotations.internal;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Inform annotation processor that this service or class associated with this service can access classes of another plugin/s.
 * Some platforms may require these plugins to be added between soft dependencies. Only optional dependencies can be specified,
 * the library should not force the plugin to have any dependencies.
 */
@ApiStatus.Internal
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@Repeatable(AccessPluginClasses.List.class)
public @interface AccessPluginClasses {
    /**
     * @return soft dependencies
     */
    @NotNull String @NotNull [] value();

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.TYPE)
    @interface List {
        @NotNull AccessPluginClasses @NotNull [] value();
    }
}
