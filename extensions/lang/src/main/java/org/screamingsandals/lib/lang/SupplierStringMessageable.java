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

package org.screamingsandals.lib.lang;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

@Data
@RequiredArgsConstructor(staticName = "of")
public final class SupplierStringMessageable implements Messageable {
    private final @NotNull Supplier<@NotNull List<@NotNull String>> supplier;
    private final @NotNull Type type;

    public static @NotNull SupplierStringMessageable of(@NotNull Supplier<@NotNull List<@NotNull String>> message) {
        return of(message, Type.LEGACY);
    }

    @Override
    public @NotNull List<@NotNull String> getKeys() {
        return supplier.get();
    }

    @Override
    public boolean needsTranslation() {
        return false;
    }
}
