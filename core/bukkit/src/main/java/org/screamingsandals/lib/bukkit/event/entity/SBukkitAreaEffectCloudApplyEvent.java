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
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SAreaEffectCloudApplyEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

import java.util.Collection;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class SBukkitAreaEffectCloudApplyEvent implements SAreaEffectCloudApplyEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull AreaEffectCloudApplyEvent event;

    // Internal cache
    private @Nullable EntityBasic entity;
    private @Nullable Collection<@NotNull EntityBasic> affectedEntities;

    @Override
    public @NotNull EntityBasic entity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public @NotNull Collection<@NotNull EntityBasic> affectedEntities() {
        if (affectedEntities == null) {
            affectedEntities = new CollectionLinkedToCollection<>(
                    event.getAffectedEntities(),
                    entityBasic -> entityBasic.as(LivingEntity.class),
                    livingEntity -> EntityMapper.wrapEntity(livingEntity).orElseThrow()
            );
        }
        return affectedEntities;
    }
}
