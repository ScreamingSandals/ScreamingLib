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

package org.screamingsandals.lib.utils.annotations.parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The ConfigFile annotation is used to mark parameter in constructor or init method of {@link org.screamingsandals.lib.utils.annotations.Service}
 * which can be assigned to path of file in plugin's data folder. The parameter type must be {@link java.nio.file.Path}
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface ConfigFile {
    /**
     * Returns path of the file
     *
     * @return the path of the file
     */
    String value();

    /**
     * It's possible to move file from old location to new one via this annotation.
     *
     * @return the old path of the file
     */
    String old() default "";

    /**
     * If the annotated field is ConfigurationLoader, should ScreamingLib serializers be included?
     *
     * @return should ScreamingLib serializers be included
     */
    boolean screamingLibSerializers() default true;
}
