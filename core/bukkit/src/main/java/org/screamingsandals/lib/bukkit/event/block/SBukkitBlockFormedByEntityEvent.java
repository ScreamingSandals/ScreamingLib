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

package org.screamingsandals.lib.bukkit.event.block;

import org.bukkit.event.block.EntityBlockFormEvent;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.block.SBlockFormedByEntityEvent;

public class SBukkitBlockFormedByEntityEvent extends SBukkitBlockFormEvent implements SBlockFormedByEntityEvent {
    // Internal cache
    private EntityBasic producer;

    public SBukkitBlockFormedByEntityEvent(EntityBlockFormEvent event) {
        super(event);
    }

    @Override
    public EntityBasic getProducer() {
        if (producer == null) {
            producer = EntityMapper.wrapEntity(((EntityBlockFormEvent) getEvent()).getEntity()).orElseThrow();
        }
        return producer;
    }
}
