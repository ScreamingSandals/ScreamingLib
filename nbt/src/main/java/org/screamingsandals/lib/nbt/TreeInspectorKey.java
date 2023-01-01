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

package org.screamingsandals.lib.nbt;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface TreeInspectorKey<T extends Tag> {
    static @NotNull <T extends Tag> TreeInspectorKey<T> of(@NotNull Class<T> tagClass, @NotNull String @NotNull... tagKeys) {
        return new TreeInspectorKeyImpl<>(tagClass, tagKeys);
    }

    static @NotNull <T extends Tag> TreeInspectorKeyLazyImpl<T> of(@NotNull Class<T> tagClass, @NotNull Supplier<@NotNull String @NotNull []> tagKeysSupplier) {
        return new TreeInspectorKeyLazyImpl<>(tagClass, tagKeysSupplier);
    }

    @NotNull Class<T> getTagClass();

    @NotNull String @NotNull [] getTagKeys();
}
