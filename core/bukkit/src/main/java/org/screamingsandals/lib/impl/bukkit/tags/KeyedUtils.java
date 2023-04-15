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

package org.screamingsandals.lib.impl.bukkit.tags;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

// To trick plugin-class-loader in legacy, all methods here are not in their respective Mapping class, that's also why we can't use generics here properly
@UtilityClass
public class KeyedUtils {
    @SuppressWarnings("unchecked")
    public static <T> boolean isTagged(@NotNull String registry, @NotNull NamespacedKey key, @NotNull Class<T> type, @NotNull T object) {
        var bukkitTag = Bukkit.getTag(registry, key, (Class<Keyed>) type);
        if (bukkitTag != null) {
            return bukkitTag.isTagged((Keyed) object);
        }
        return false;
    }
}
