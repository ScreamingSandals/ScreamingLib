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

package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.bukkit.block.BlockState;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityCreatePortalEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.PortalType;

import java.util.Collection;
import java.util.Locale;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityCreatePortalEvent implements SEntityCreatePortalEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityCreatePortalEvent event;

    // Internal cache
    private EntityBasic entity;
    private Collection<BlockStateHolder> blocks;
    private PortalType portalType;

    @Override
    public EntityBasic entity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public Collection<BlockStateHolder> blocks() {
        if (blocks == null) {
            blocks = new CollectionLinkedToCollection<>(
                    event.getBlocks(),
                    blockStateHolder -> blockStateHolder.as(BlockState.class),
                    blockState -> BlockStateMapper.wrapBlockState(blockState).orElseThrow()
            );
        }
        return blocks;
    }

    @Override
    public PortalType portalType() {
        if (portalType == null) {
            portalType = PortalType.valueOf(event.getPortalType().name().toUpperCase(Locale.ROOT));
        }
        return portalType;
    }
}
