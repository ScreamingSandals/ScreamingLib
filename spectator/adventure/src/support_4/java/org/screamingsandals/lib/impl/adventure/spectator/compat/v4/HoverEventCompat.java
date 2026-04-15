/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.impl.adventure.spectator.compat.v4;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@UtilityClass
public class HoverEventCompat {
    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    public static HoverEvent.@NotNull ShowItem showItem(@NotNull Key key, int count, @Nullable BinaryTagHolder nbt) {
        return HoverEvent.ShowItem.of(key, count, nbt);
    }

    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    public static HoverEvent.@NotNull ShowEntity showEntity(@NotNull Key key, @NotNull UUID id, @Nullable Component component) {
        return HoverEvent.ShowEntity.of(key, id, component);
    }
}
