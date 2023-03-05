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

package org.screamingsandals.lib.proxy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.Wrapper;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

public interface ServerWrapper extends Wrapper {

    @NotNull String getName();

    @NotNull SocketAddress getSocketAddress();

    /**
     * @return optional containing InetSocketAddress or empty optional if connection is done by Unix domain socket
     */
    default @Nullable InetSocketAddress getAddress() {
        var address = getSocketAddress();
        if (address instanceof InetSocketAddress) {
            return (InetSocketAddress) address;
        } else {
            return null;
        }
    }

    default @NotNull List<@NotNull ProxiedPlayerWrapper> getPlayers() {
        return ProxiedPlayerMapper.getPlayers(this);
    }
}
