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

package org.screamingsandals.lib.velocity.proxy;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.proxy.ProxiedPlayerWrapper;
import org.screamingsandals.lib.proxy.ServerWrapper;
import org.screamingsandals.lib.spectator.audience.adapter.PlayerAdapter;

import java.util.Locale;
import java.util.UUID;

public class VelocityProxiedPlayerWrapper extends VelocityProxiedSenderWrapper implements ProxiedPlayerWrapper {
    public VelocityProxiedPlayerWrapper(@NotNull Player wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull UUID getUuid() {
        return ((Player) wrappedObject).getUniqueId();
    }

    @Override
    public void switchServer(@NotNull ServerWrapper server) {
        ((Player) wrappedObject).createConnectionRequest(server.as(RegisteredServer.class));
    }

    @Override
    public @NotNull Locale getLocale() {
        return ((Player) wrappedObject).getPlayerSettings().getLocale();
    }

    @Override
    public @NotNull String getName() {
        return ((Player) wrappedObject).getUsername();
    }

    @Override
    public @NotNull PlayerAdapter adapter() {
        return (PlayerAdapter) super.adapter();
    }
}
