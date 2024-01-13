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

package org.screamingsandals.lib.impl.utils.feature;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

@RequiredArgsConstructor(staticName = "of")
public final class PlatformFeature {
    private final @NotNull BooleanSupplier supplier;
    private @Nullable Boolean value;

    public boolean isSupported() {
        if (value == null) {
            value = supplier.getAsBoolean();
        }
        return value;
    }

    public @NotNull PlatformFeature and(@NotNull BooleanSupplier supplier) {
        return new PlatformFeature(() -> this.isSupported() && supplier.getAsBoolean());
    }
}
