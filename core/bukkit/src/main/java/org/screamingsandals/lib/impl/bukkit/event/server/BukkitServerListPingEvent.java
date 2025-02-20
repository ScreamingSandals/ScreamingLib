/*
 * Copyright 2025 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.event.server;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.server.ServerListPingEvent;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.impl.bukkit.BukkitCore;
import org.screamingsandals.lib.spectator.Component;

import java.net.InetAddress;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitServerListPingEvent implements ServerListPingEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.server.ServerListPingEvent event;

    @Override
    public @NotNull InetAddress address() {
        return event.getAddress();
    }

    @Override
    public @NotNull Component description() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return AdventureBackend.wrapComponent(event.motd());
        } else {
            return Component.fromLegacy(event.getMotd());
        }
    }

    @Override
    public void description(@NotNull Component description) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            event.motd(description.as(net.kyori.adventure.text.Component.class));
        } else {
            event.setMotd(description.toLegacy());
        }
    }

    @Override
    public int maxPlayers() {
        return event.getMaxPlayers();
    }
}
