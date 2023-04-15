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

package org.screamingsandals.lib.impl.velocity.proxy.event;

import com.velocitypowered.api.event.player.PlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.NoAutoCancellable;
import org.screamingsandals.lib.impl.velocity.proxy.VelocityProxiedPlayerWrapper;
import org.screamingsandals.lib.proxy.ProxiedPlayerWrapper;
import org.screamingsandals.lib.proxy.event.SPlayerChatEvent;
import org.screamingsandals.lib.utils.BasicWrapper;

public class VelocityPlayerChatEvent extends BasicWrapper<PlayerChatEvent> implements SPlayerChatEvent, NoAutoCancellable {
    protected VelocityPlayerChatEvent(@NotNull PlayerChatEvent wrappedObject) {
        super(wrappedObject);
    }

    private @Nullable ProxiedPlayerWrapper player;

    @Override
    public @NotNull ProxiedPlayerWrapper getPlayer() {
        if (player == null) {
            player = new VelocityProxiedPlayerWrapper(wrappedObject.getPlayer());
        }
        return player;
    }

    @Override
    public boolean isCommand() {
        return wrappedObject.getMessage().startsWith("/");
    }

    @Override
    public @NotNull String getMessage() {
        return wrappedObject.getResult().getMessage().orElse(wrappedObject.getMessage());
    }

    @Override
    public void setMessage(@NotNull String message) {
        if (wrappedObject.getResult().isAllowed()) {
            wrappedObject.setResult(PlayerChatEvent.ChatResult.message(message));
        }
    }

    @Override
    public boolean cancelled() {
        return !wrappedObject.getResult().isAllowed();
    }

    @Override
    public void cancelled(boolean cancel) {
        if (cancel && wrappedObject.getResult().isAllowed()) {
            wrappedObject.setResult(PlayerChatEvent.ChatResult.denied());
        } else if (!cancel && !wrappedObject.getResult().isAllowed()) {
            wrappedObject.setResult(PlayerChatEvent.ChatResult.allowed());
        }
    }
}
