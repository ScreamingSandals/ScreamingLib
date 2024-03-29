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
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.impl.bukkit.BukkitCore;
import org.screamingsandals.lib.event.player.AsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

import java.net.InetAddress;
import java.util.UUID;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitAsyncPlayerPreLoginEvent implements AsyncPlayerPreLoginEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.AsyncPlayerPreLoginEvent event;

    @Override
    public @NotNull UUID uuid() {
        return event.getUniqueId();
    }

    @Override
    public @NotNull InetAddress address() {
        return event.getAddress();
    }

    @Override
    public @NotNull String name() {
        return event.getName();
    }

    @Override
    public @NotNull Result result() {
        return Result.valueOf(event.getLoginResult().name());
    }

    @Override
    public void result(@NotNull Result result) {
        event.setLoginResult(org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.valueOf(result.name()));
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
    public void message(@NotNull Component message) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            event.kickMessage(message.as(net.kyori.adventure.text.Component.class));
        } else {
            event.setKickMessage(message.toLegacy());
        }
    }

    @Override
    public void message(@NotNull ComponentLike message) {
        message(message.asComponent()); // TODO: auto localize??
    }
}
