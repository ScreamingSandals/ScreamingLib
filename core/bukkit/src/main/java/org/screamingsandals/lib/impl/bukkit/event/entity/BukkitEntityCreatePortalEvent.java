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

package org.screamingsandals.lib.impl.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.snapshot.BlockSnapshot;
import org.screamingsandals.lib.impl.block.snapshot.BlockSnapshots;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.entity.EntityCreatePortalEvent;
import org.screamingsandals.lib.impl.utils.collections.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.PortalType;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

import java.util.Collection;
import java.util.Locale;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitEntityCreatePortalEvent implements EntityCreatePortalEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.entity.EntityCreatePortalEvent event;

    // Internal cache
    private @Nullable Entity entity;
    private @Nullable Collection<@NotNull BlockSnapshot> blocks;
    private @Nullable PortalType portalType;

    @Override
    public @NotNull Entity entity() {
        if (entity == null) {
            entity = Entities.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public @NotNull Collection<@NotNull BlockSnapshot> blocks() {
        if (blocks == null) {
            blocks = new CollectionLinkedToCollection<>(
                    event.getBlocks(),
                    blockStateHolder -> blockStateHolder.as(BlockState.class),
                    blockState -> BlockSnapshots.<BlockSnapshot>wrapBlockSnapshot(blockState).orElseThrow()
            );
        }
        return blocks;
    }

    @Override
    public @NotNull PortalType portalType() {
        if (portalType == null) {
            portalType = PortalType.valueOf(event.getPortalType().name().toUpperCase(Locale.ROOT));
        }
        return portalType;
    }
}
