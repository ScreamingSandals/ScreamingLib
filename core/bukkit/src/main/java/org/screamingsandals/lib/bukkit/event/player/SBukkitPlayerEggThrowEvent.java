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

package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.event.player.SPlayerEggThrowEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerEggThrowEvent implements SPlayerEggThrowEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerEggThrowEvent event;

    // Internal cache
    private PlayerWrapper player;
    private EntityBasic egg;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public EntityBasic getEgg() {
        if (egg == null) {
            egg = EntityMapper.wrapEntity(event.getEgg()).orElseThrow();
        }
        return egg;
    }

    @Override
    public boolean isHatching() {
        return event.isHatching();
    }

    @Override
    public void setHatching(boolean hatching) {
        event.setHatching(hatching);
    }

    @Override
    public EntityTypeHolder getHatchType() {
        return EntityTypeHolder.of(event.getHatchingType());
    }

    @Override
    public void setHatchType(EntityTypeHolder hatchType) {
        event.setHatchingType(hatchType.as(EntityType.class));
    }

    @Override
    public byte getNumHatches() {
        return event.getNumHatches();
    }

    @Override
    public void setNumHatches(byte numHatches) {
        event.setNumHatches(numHatches);
    }
}
