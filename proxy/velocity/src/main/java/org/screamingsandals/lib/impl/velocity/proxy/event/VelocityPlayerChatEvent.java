/*
 * Copyright 2024 ScreamingSandals
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.velocity.proxy.VelocityProxiedPlayer;
import org.screamingsandals.lib.proxy.ProxiedPlayer;
import org.screamingsandals.lib.proxy.event.PlayerChatEvent;
import org.screamingsandals.lib.utils.BasicWrapper;

public class VelocityPlayerChatEvent extends BasicWrapper<com.velocitypowered.api.event.player.PlayerChatEvent> implements PlayerChatEvent {
    protected VelocityPlayerChatEvent(@NotNull com.velocitypowered.api.event.player.PlayerChatEvent wrappedObject) {
        super(wrappedObject);
    }

    private @Nullable ProxiedPlayer player;

    @Override
    public @NotNull ProxiedPlayer getPlayer() {
        if (player == null) {
            player = new VelocityProxiedPlayer(wrappedObject.getPlayer());
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
            wrappedObject.setResult(com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult.message(message));
        }
    }

    @Override
    public boolean cancelled() {
        return !wrappedObject.getResult().isAllowed();
    }

    @Override
    public void cancelled(boolean cancel) {
        if (cancel && wrappedObject.getResult().isAllowed()) {
            wrappedObject.setResult(com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult.denied());
        } else if (!cancel && !wrappedObject.getResult().isAllowed()) {
            wrappedObject.setResult(com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult.allowed());
        }
    }
}
