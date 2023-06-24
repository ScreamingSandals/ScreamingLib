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

package org.screamingsandals.lib.impl.bungee.proxy.listener;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bungee.proxy.BungeeProxiedPlayer;
import org.screamingsandals.lib.proxy.ProxiedPlayer;
import org.screamingsandals.lib.proxy.event.PlayerLeaveEvent;
import org.screamingsandals.lib.utils.BasicWrapper;

public class BungeePlayerLeaveEvent extends BasicWrapper<PlayerDisconnectEvent> implements PlayerLeaveEvent {
    protected BungeePlayerLeaveEvent(@NotNull PlayerDisconnectEvent wrappedObject) {
        super(wrappedObject);
    }

    private @Nullable ProxiedPlayer player;

    @Override
    public @NotNull LoginStatus getStatus() {
        return LoginStatus.CANCELLED_BY_USER;
    }

    @Override
    public @NotNull ProxiedPlayer getPlayer() {
        if (player == null) {
            player = new BungeeProxiedPlayer(wrappedObject.getPlayer());
        }
        return player;
    }
}
