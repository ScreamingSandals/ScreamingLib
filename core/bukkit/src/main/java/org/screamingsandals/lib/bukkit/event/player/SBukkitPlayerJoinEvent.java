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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.bukkit.BukkitCore;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.event.player.SPlayerJoinEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerJoinEvent implements SPlayerJoinEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerJoinEvent event;

    // Internal cache
    private PlayerWrapper player;

    @Override
    public PlayerWrapper player() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    @Nullable
    public Component joinMessage() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return AdventureBackend.wrapComponent(event.joinMessage());
        } else {
            return Component.fromLegacy(event.getJoinMessage());
        }
    }

    @Override
    public void joinMessage(@Nullable Component joinMessage) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            event.joinMessage(joinMessage != null ? joinMessage.as(net.kyori.adventure.text.Component.class) : null);
        } else {
            event.setJoinMessage(joinMessage != null ? joinMessage.toLegacy() : null);
        }
    }

    @Override
    public void joinMessage(@Nullable ComponentLike joinMessage) {
        if (joinMessage instanceof AudienceComponentLike) {
            // TODO: there should be another logic, because this message can be seen by more players
            joinMessage(((AudienceComponentLike) joinMessage).asComponent(player()));
        } else {
            joinMessage(joinMessage != null ? joinMessage.asComponent() : null);
        }
    }
}
