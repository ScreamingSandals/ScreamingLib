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

package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import lombok.experimental.Accessors;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.bukkit.BukkitCore;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerKickEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerKickEvent implements SPlayerKickEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerKickEvent event;

    // Internal cache
    private PlayerWrapper player;

    @Override
    public @NotNull PlayerWrapper player() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public Component leaveMessage() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return AdventureBackend.wrapComponent(event.leaveMessage());
        } else {
            return Component.fromLegacy(event.getLeaveMessage());
        }
    }

    @Override
    public void leaveMessage(Component leaveMessage) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            event.leaveMessage(leaveMessage.as(net.kyori.adventure.text.Component.class));
        } else {
            event.setLeaveMessage(leaveMessage.toLegacy());
        }

    }

    @Override
    public void leaveMessage(ComponentLike leaveMessage) {
        if (leaveMessage instanceof AudienceComponentLike) {
            // TODO: there should be another logic, because this message can be seen by more players
            leaveMessage(((AudienceComponentLike) leaveMessage).asComponent(player()));
        } else {
            leaveMessage(leaveMessage.asComponent());
        }
    }

    @Override
    public Component kickReason() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return AdventureBackend.wrapComponent(event.reason());
        } else {
            return Component.fromLegacy(event.getReason());
        }
    }

    @Override
    public void kickReason(Component kickReason) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            event.reason(kickReason.as(net.kyori.adventure.text.Component.class));
        } else {
            event.setReason(kickReason.toLegacy());
        }
    }

    @Override
    public void kickReason(ComponentLike kickReason) {
        if (kickReason instanceof AudienceComponentLike) {
            kickReason(((AudienceComponentLike) kickReason).asComponent(player()));
        } else {
            kickReason(kickReason.asComponent());
        }
    }
}
