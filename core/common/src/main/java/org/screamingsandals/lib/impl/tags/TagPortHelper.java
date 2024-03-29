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

package org.screamingsandals.lib.impl.tags;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Data
public class TagPortHelper {
    private final @NotNull Predicate<@NotNull String> nativeTagChecker;
    private @Nullable List<@NotNull String> ports;

    public void port(@NotNull String tag) {
        if (ports == null) {
            ports = new ArrayList<>();
        }
        ports.add(tag);
    }

    public boolean hasTag(@NotNull String tag) {
        if (ports != null && ports.contains(tag)) {
            return true;
        }
        return nativeTagChecker.test(tag);
    }

    public boolean hasTag(@NotNull String @NotNull... tags) {
        for (var tag : tags) {
            if (hasTag(tag)) {
                return true;
            }
        }
        return false;
    }
}
