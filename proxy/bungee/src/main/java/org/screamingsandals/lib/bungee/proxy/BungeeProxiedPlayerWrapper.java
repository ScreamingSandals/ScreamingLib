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

package org.screamingsandals.lib.bungee.proxy;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.proxy.ProxiedPlayerWrapper;
import org.screamingsandals.lib.proxy.ServerWrapper;
import org.screamingsandals.lib.spectator.audience.adapter.PlayerAdapter;

import java.util.Locale;
import java.util.UUID;

public class BungeeProxiedPlayerWrapper extends BungeeProxiedSenderWrapper implements ProxiedPlayerWrapper {
    public BungeeProxiedPlayerWrapper(@NotNull ProxiedPlayer wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull UUID getUuid() {
        return ((ProxiedPlayer) wrappedObject).getUniqueId();
    }

    @Override
    public void switchServer(@NotNull ServerWrapper server) {
        ((ProxiedPlayer) wrappedObject).connect(server.as(ServerInfo.class));
    }

    @Override
    public @NotNull Locale getLocale() {
        return ((ProxiedPlayer) wrappedObject).getLocale();
    }

    @Override
    public @NotNull PlayerAdapter adapter() {
        return (PlayerAdapter) super.adapter();
    }
}
