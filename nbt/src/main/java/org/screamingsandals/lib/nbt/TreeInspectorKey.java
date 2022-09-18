/*
 * Copyright 2022 ScreamingSandals
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

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class TreeInspectorKey<T extends Tag> {
    private final @NotNull Class<T> tagClass;
    private final @NotNull String @NotNull [] tagKeys;

    public static @NotNull <T extends Tag> TreeInspectorKey<T> of(@NotNull Class<T> tagClass, @NotNull String @NotNull... tagKeys) {
        return new TreeInspectorKey<>(tagClass, tagKeys);
    }
}
