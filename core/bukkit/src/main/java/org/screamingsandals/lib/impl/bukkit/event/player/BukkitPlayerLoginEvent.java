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
import org.screamingsandals.lib.event.player.AsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.event.player.PlayerLoginEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

import java.net.InetAddress;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerLoginEvent implements PlayerLoginEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerLoginEvent event;

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
    public @NotNull InetAddress address() {
        return event.getAddress();
    }

    @Override
    public @NotNull String hostname() {
        return event.getHostname();
    }

    @Override
    public AsyncPlayerPreLoginEvent.@NotNull Result result() {
        return AsyncPlayerPreLoginEvent.Result.valueOf(event.getResult().name());
    }

    @Override
    public void result(AsyncPlayerPreLoginEvent.@NotNull Result result) {
        event.setResult(org.bukkit.event.player.PlayerLoginEvent.Result.valueOf(result.name()));
    }

    @Override
    public @NotNull Component message() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return AdventureBackend.wrapComponent(event.kickMessage());
        } else {
            return Component.fromLegacy(event.getKickMessage());
        }
    }

    @Override
    public void message(@NotNull Component kickMessage) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            event.kickMessage(kickMessage.as(net.kyori.adventure.text.Component.class));
        } else {
            event.setKickMessage(kickMessage.toLegacy());
        }
    }

    @Override
    public void message(@NotNull ComponentLike message) {
        if (message instanceof AudienceComponentLike) {
            message(((AudienceComponentLike) message).asComponent(player()));
        } else {
            message(message.asComponent());
        }
    }
}
