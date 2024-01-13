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

package org.screamingsandals.lib.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class Preconditions {
    @Contract("false -> fail")
    public void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    @Contract("false, _ -> fail")
    public void checkArgument(boolean expression, @NotNull Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    @Contract("null -> fail; _ -> param1")
    public <T> @NotNull T checkNotNull(@Nullable T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    @Contract("null, _ -> fail; _, _ -> param1")
    public <T> @NotNull T checkNotNull(@Nullable T reference, @NotNull Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    @Contract("null -> fail; _ -> param1")
    public <T> T checkNotNullIllegal(@Nullable T reference) {
        if (reference == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        return reference;
    }

    @Contract("null, _ -> fail; _, _ -> param1")
    public <T> T checkNotNullIllegal(@Nullable T reference, @NotNull Object errorMessage) {
        if (reference == null) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
        return reference;
    }
}
