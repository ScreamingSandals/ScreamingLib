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

package org.screamingsandals.lib.impl.bungee.proxy;

import net.md_5.bungee.api.config.ServerInfo;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.proxy.ServerWrapper;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.net.SocketAddress;

public class BungeeServerWrapper extends BasicWrapper<ServerInfo> implements ServerWrapper {
    public BungeeServerWrapper(@NotNull ServerInfo wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String getName() {
        return wrappedObject.getName();
    }

    @Override
    public @NotNull SocketAddress getSocketAddress() {
        return wrappedObject.getSocketAddress();
    }
}
