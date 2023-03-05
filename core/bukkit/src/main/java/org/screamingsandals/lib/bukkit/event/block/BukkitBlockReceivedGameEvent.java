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

package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.bukkit.event.block.BlockReceiveGameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.bukkit.block.BukkitBlock;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.BasicEntity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.block.BlockReceivedGameEvent;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitBlockReceivedGameEvent implements BlockReceivedGameEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull BlockReceiveGameEvent event;

    // Internal cache
    private @Nullable Block block;
    private @Nullable BasicEntity entity;
    private boolean entityConverted;
    private @Nullable NamespacedMappingKey underlyingEvent;

    @Override
    public @NotNull Block block() {
        if (block == null) {
            block = new BukkitBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public @Nullable BasicEntity entity() {
        if (!entityConverted) {
            if (event.getEntity() != null) {
                entity = Entities.wrapEntity(event.getEntity()).orElseThrow();
            }
            entityConverted = true;
        }
        return entity;
    }

    @Override
    public @NotNull NamespacedMappingKey underlyingEvent() {
        if (underlyingEvent == null) {
            underlyingEvent = NamespacedMappingKey.of(event.getEvent().getKey().toString());
        }
        return underlyingEvent;
    }
}
