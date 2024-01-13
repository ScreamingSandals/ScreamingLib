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

import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.impl.bukkit.BukkitCore;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.PlayerKickEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerKickEvent implements PlayerKickEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerKickEvent event;

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
    public @NotNull Component leaveMessage() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return AdventureBackend.wrapComponent(event.leaveMessage());
        } else {
            return Component.fromLegacy(event.getLeaveMessage());
        }
    }

    @Override
    public void leaveMessage(@NotNull Component leaveMessage) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            event.leaveMessage(leaveMessage.as(net.kyori.adventure.text.Component.class));
        } else {
            event.setLeaveMessage(leaveMessage.toLegacy());
        }

    }

    @Override
    public void leaveMessage(@NotNull ComponentLike leaveMessage) {
        if (leaveMessage instanceof AudienceComponentLike) {
            // TODO: there should be another logic, because this message can be seen by more players
            leaveMessage(((AudienceComponentLike) leaveMessage).asComponent(player()));
        } else {
            leaveMessage(leaveMessage.asComponent());
        }
    }

    @Override
    public @NotNull Component kickReason() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return AdventureBackend.wrapComponent(event.reason());
        } else {
            return Component.fromLegacy(event.getReason());
        }
    }

    @Override
    public void kickReason(@NotNull Component kickReason) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            event.reason(kickReason.as(net.kyori.adventure.text.Component.class));
        } else {
            event.setReason(kickReason.toLegacy());
        }
    }

    @Override
    public void kickReason(@NotNull ComponentLike kickReason) {
        if (kickReason instanceof AudienceComponentLike) {
            kickReason(((AudienceComponentLike) kickReason).asComponent(player()));
        } else {
            kickReason(kickReason.asComponent());
        }
    }
}
