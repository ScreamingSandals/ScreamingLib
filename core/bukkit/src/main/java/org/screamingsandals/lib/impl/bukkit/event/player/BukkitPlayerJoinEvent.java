/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.impl.bukkit.BukkitCore;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.event.player.PlayerJoinEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerJoinEvent implements PlayerJoinEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerJoinEvent event;

    // Internal cache
    private @Nullable Player player;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @Nullable Component joinMessage() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return AdventureBackend.wrapComponent(event.joinMessage());
        } else {
            var joinMessage = event.getJoinMessage();
            return joinMessage != null ? Component.fromLegacy(joinMessage) : null;
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
