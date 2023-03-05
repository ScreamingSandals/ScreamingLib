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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.block.Blocks;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.BasicEntity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.entity.EntityExplodeEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.Locations;

import java.util.Collection;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitEntityExplodeEvent implements EntityExplodeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.entity.EntityExplodeEvent event;

    // Internal cache
    private @Nullable BasicEntity entity;
    private @Nullable Location location;
    private @Nullable Collection<@NotNull Block> blocks;

    @Override
    public @NotNull BasicEntity entity() {
        if (entity == null) {
            entity = Entities.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public @NotNull Location location() {
        if (location == null) {
            location = Locations.wrapLocation(event.getLocation());
        }
        return location;
    }

    @Override
    public @NotNull Collection<@NotNull Block> blocks() {
        if (blocks == null) {
            blocks = new CollectionLinkedToCollection<>(event.blockList(), o -> o.as(org.bukkit.block.Block.class), Blocks::wrapBlock);
        }
        return blocks;
    }

    @Override
    public float yield() {
        return event.getYield();
    }

    @Override
    public void yield(float yield) {
        event.setYield(yield);
    }
}
