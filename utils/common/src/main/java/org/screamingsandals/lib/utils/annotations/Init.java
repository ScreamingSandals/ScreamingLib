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

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.PlatformType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.TYPE,
        ElementType.PACKAGE
})
@Repeatable(Init.List.class)
public @interface Init {
    /**
     * Contains all platforms which need these services. If empty, it means all platforms
     *
     * @return All platforms which need these services
     */
    PlatformType[] platforms() default {};

    /**
     * Contains all services which should be initialized
     *
     * @return All services which should be initialized
     */
    Class<?>[] services();

    /**
     * Contains all packages which should be initialized. These packages are annotated using {@link Init} annotation which contains another packages or services.
     *
     * @return All packages which should be initialized
     */
    String[] packages() default {};

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.TYPE)
    @ApiStatus.Internal
    @interface List {
        Init[] value();
    }
}
