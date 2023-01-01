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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AbstractService {
    /**
     * Contains pattern which will be used to resolve PlatformMapping
     * This pattern consists of three named groups. These groups will be used in replace rule.
     * <p>
     * Default naming:
     * <ul>
     *   <li>basePackage</li>
     *   <li>subPackage</li>
     *   <li>className</li>
     * </ul>
     *
     * @return regex pattern
     */
    String pattern() default "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+)\\.(?<className>.+)$";

    /**
     * Contains replace rule which will be used to resolve PlatformMapping
     * <p>
     * Default naming:
     * <ul>
     *   <li>basePackage</li>
     *   <li>subPackage</li>
     *   <li>className</li>
     *   <li>platform - lowered platform name (eg. bukkit)</li>
     *   <li>Platform - platform name with first letter capital (eg. Bukkit)</li>
     * </ul>
     *
     * @return replace rule
     */
    String replaceRule() default "{basePackage}.{platform}.{subPackage}.{Platform}{className}";
}
