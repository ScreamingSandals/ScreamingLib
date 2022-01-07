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

import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.entity.SEntityCombustByBlockEvent;

import lombok.experimental.Accessors;

public class SBukkitEntityCombustByBlockEvent extends SBukkitEntityCombustEvent implements SEntityCombustByBlockEvent {
    public SBukkitEntityCombustByBlockEvent(EntityCombustByBlockEvent event) {
        super(event);
    }

    // Internal cache
    private BlockHolder combuster;

    @Override
    public BlockHolder combuster() {
        if (combuster == null) {
            combuster = BlockMapper.wrapBlock(((EntityCombustByBlockEvent) event()).getCombuster());
        }
        return combuster;
    }
}
