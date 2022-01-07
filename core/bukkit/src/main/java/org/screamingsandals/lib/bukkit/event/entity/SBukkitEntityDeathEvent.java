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
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.bukkit.event.NoAutoCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityDeathEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityDeathEvent implements SEntityDeathEvent, NoAutoCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityDeathEvent event;

    // Internal cache
    private EntityBasic entity;
    private Collection<Item> drops;

    @Override
    public Collection<Item> getDrops() {
        if (drops == null) {
            drops = new CollectionLinkedToCollection<>(
                    event.getDrops(),
                    item -> item.as(ItemStack.class),
                    BukkitItem::new
            );
        }
        return drops;
    }

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public int getDropExp() {
        return event.getDroppedExp();
    }

    @Override
    public void setDropExp(int dropExp) {
        event.setDroppedExp(dropExp);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean cancelled() {
        if (event instanceof Cancellable) { // event is cancellable only in Paper
            return ((Cancellable) event).isCancelled();
        }
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void cancelled(boolean cancel) {
        if (event instanceof Cancellable) { // event is cancellable only in Paper
            ((Cancellable) event).setCancelled(cancel);
        }
    }
}
