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

package org.screamingsandals.lib.velocity.proxy.event;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.NoAutoCancellable;
import org.screamingsandals.lib.proxy.ProxiedPlayerWrapper;
import org.screamingsandals.lib.proxy.event.SPlayerLeaveEvent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.velocity.proxy.VelocityProxiedPlayerWrapper;

public class VelocityPlayerLeaveEvent extends BasicWrapper<DisconnectEvent> implements SPlayerLeaveEvent, NoAutoCancellable {
    protected VelocityPlayerLeaveEvent(@NotNull DisconnectEvent wrappedObject) {
        super(wrappedObject);
    }

    private @Nullable ProxiedPlayerWrapper player;
    private @Nullable LoginStatus status;

    @Override
    public @NotNull LoginStatus getStatus() {
        if (status == null) {
            status = LoginStatus.convert(wrappedObject.getLoginStatus().name());
        }
        return status;
    }

    @Override
    public @NotNull ProxiedPlayerWrapper getPlayer() {
        if (player == null) {
            player = new VelocityProxiedPlayerWrapper(wrappedObject.getPlayer());
        }
        return player;
    }
}
