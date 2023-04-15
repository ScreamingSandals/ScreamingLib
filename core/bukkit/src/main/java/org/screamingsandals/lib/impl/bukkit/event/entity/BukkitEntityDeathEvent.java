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
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.NoAutoCancellable;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.entity.EntityDeathEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.impl.utils.collections.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

import java.util.Collection;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitEntityDeathEvent implements EntityDeathEvent, NoAutoCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.entity.EntityDeathEvent event;

    // Internal cache
    private @Nullable Entity entity;
    private @Nullable Collection<@NotNull ItemStack> drops;

    @Override
    public @NotNull Collection<@NotNull ItemStack> drops() {
        if (drops == null) {
            drops = new CollectionLinkedToCollection<>(
                    event.getDrops(),
                    item -> item.as(org.bukkit.inventory.ItemStack.class),
                    BukkitItem::new
            );
        }
        return drops;
    }

    @Override
    public @NotNull Entity entity() {
        if (entity == null) {
            entity = Entities.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public int dropExp() {
        return event.getDroppedExp();
    }

    @Override
    public void dropExp(int dropExp) {
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
