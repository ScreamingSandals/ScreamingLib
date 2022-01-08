/*
 * Copyright 2022 ScreamingSandals
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

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProxiedPlayerWrapper extends ProxiedSenderWrapper {
    private final UUID uuid;

    public ProxiedPlayerWrapper(String name, UUID uuid) {
        super(name, Type.PLAYER);
        this.uuid = uuid;
    }

    public void sendMessage(String message) {
        ProxiedPlayerMapper.sendMessage(this, message);
    }

    public void switchServer(ServerWrapper server) {
        ProxiedPlayerMapper.switchServer(this, server);
    }

    public <T> T as(Class<T> type) {
        return ProxiedPlayerMapper.convertPlayerWrapper(this, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProxiedPlayerWrapper)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return ((ProxiedPlayerWrapper) obj).uuid.equals(this.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
