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

package org.screamingsandals.lib.impl.proxy;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.proxy.ProxiedPlayer;
import org.screamingsandals.lib.proxy.Server;
import org.screamingsandals.lib.utils.annotations.ProvidedService;

import java.util.List;
import java.util.UUID;

@ProvidedService
public abstract class ProxiedPlayerMapper {

    private static @Nullable ProxiedPlayerMapper proxiedPlayerMapper;

    @ApiStatus.Internal
    public ProxiedPlayerMapper() {
        if (proxiedPlayerMapper != null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper are already initialized.");
        }

        proxiedPlayerMapper = this;
    }

    public static @Nullable Server getServer(@NotNull String name) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getServer0(name);
    }

    public abstract @Nullable Server getServer0(@NotNull String name);

    public static @NotNull List<@NotNull Server> getServers() {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getServers0();
    }

    public abstract @NotNull List<@NotNull Server> getServers0();

    public static @Nullable ProxiedPlayer getPlayer(@NotNull String name) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getPlayer0(name);
    }

    public abstract @Nullable ProxiedPlayer getPlayer0(@NotNull String name);

    public static @Nullable ProxiedPlayer getPlayer(@NotNull UUID uuid) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getPlayer0(uuid);
    }

    public abstract @Nullable ProxiedPlayer getPlayer0(@NotNull UUID uuid);

    public static @NotNull List<@NotNull ProxiedPlayer> getPlayers() {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getPlayers0();
    }

    public abstract @NotNull List<@NotNull ProxiedPlayer> getPlayers0();

    public static @NotNull List<@NotNull ProxiedPlayer> getPlayers(@NotNull Server serverWrapper) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getPlayers0(serverWrapper);
    }

    public abstract @NotNull List<@NotNull ProxiedPlayer> getPlayers0(@NotNull Server serverWrapper);
}
