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

package org.screamingsandals.lib.bungee.proxy.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bungee.proxy.BungeeProxiedPlayerWrapper;
import org.screamingsandals.lib.proxy.ProxiedPlayerWrapper;
import org.screamingsandals.lib.proxy.event.SPlayerChatEvent;
import org.screamingsandals.lib.utils.BasicWrapper;

public class BungeePlayerChatEvent extends BasicWrapper<ChatEvent> implements SPlayerChatEvent {
    protected BungeePlayerChatEvent(@NotNull ChatEvent wrappedObject) {
        super(wrappedObject);
    }

    private @Nullable ProxiedPlayerWrapper player;

    @Override
    public @NotNull ProxiedPlayerWrapper getPlayer() {
        if (player == null) {
            player = new BungeeProxiedPlayerWrapper((ProxiedPlayer) wrappedObject.getSender());
        }
        return player;
    }

    @Override
    public boolean isCommand() {
        return wrappedObject.isCommand();
    }

    @Override
    public @NotNull String getMessage() {
        return wrappedObject.getMessage();
    }

    @Override
    public void setMessage(@NotNull String message) {
        wrappedObject.setMessage(message);
    }

    @Override
    public boolean cancelled() {
        return wrappedObject.isCancelled();
    }

    @Override
    public void cancelled(boolean cancel) {
        wrappedObject.setCancelled(cancel);
    }
}
