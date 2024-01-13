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

import java.lang.annotation.*;

/**
 * Marks a SAM interface as a target for lambda expressions
 * where the single parameter is passed as the implicit receiver of the
 * invocation ({@code this} in Kotlin, {@code delegate} in Groovy) as if
 * the lambda expression was an extension method of the parameter type.
 *
 * <p>
 *
 * Kotlin SAM-with-receiver plugin should be used in order to make this work with Kotlin:
 * <a href="https://kotlinlang.org/docs/sam-with-receiver-plugin.html">https://kotlinlang.org/docs/sam-with-receiver-plugin.html</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ImplicitReceiver {
}
