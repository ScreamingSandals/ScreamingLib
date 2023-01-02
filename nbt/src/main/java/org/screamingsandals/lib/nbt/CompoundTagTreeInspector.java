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
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@FunctionalInterface
public interface CompoundTagTreeInspector {
    @Nullable Tag findTag(@NotNull String @NotNull... tagKeys);

    default @Nullable Tag findTag(@NotNull Collection<@NotNull String> tagKeys) {
        return findTag(tagKeys.toArray(String[]::new));
    }

    default <T extends Tag> @Nullable T findTag(@NotNull TreeInspectorKey<T> inspectorKey) {
        var tag = findTag(inspectorKey.getTagKeys());
        if (inspectorKey.getTagClass().isInstance(tag)) {
            return inspectorKey.getTagClass().cast(tag);
        }
        return null;
    }

    default <T extends Tag> @Nullable T findTag(@NotNull Class<T> tagClass, @NotNull String @NotNull... tagKeys) {
        var tag = findTag(tagKeys);
        if (tagClass.isInstance(tag)) {
            return tagClass.cast(tag);
        }
        return null;
    }

    default <T extends Tag> @Nullable T findTag(@NotNull Class<T> tagClass, @NotNull Collection<@NotNull String> tagKeys) {
        var tag = findTag(tagKeys.toArray(String[]::new));
        if (tagClass.isInstance(tag)) {
            return tagClass.cast(tag);
        }
        return null;
    }
}
