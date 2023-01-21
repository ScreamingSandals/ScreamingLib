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

package org.screamingsandals.lib.bukkit.event.entity;

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.event.entity.ItemMergeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityItem;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.entity.SItemMergeEvent;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitItemMergeEvent implements SItemMergeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull ItemMergeEvent event;

    // Internal cache
    private @Nullable EntityItem entity;
    private @Nullable EntityItem target;

    @Override
    public @NotNull EntityItem entity() {
        if (entity == null) {
            entity = new BukkitEntityItem(event.getEntity());
        }
        return entity;
    }

    @Override
    public @NotNull EntityItem target() {
        if (target == null) {
            target = new BukkitEntityItem(event.getTarget());
        }
        return target;
    }
}
