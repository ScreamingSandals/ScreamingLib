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
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.proxy.event.SPlayerChatEvent;
import org.screamingsandals.lib.impl.velocity.event.AbstractVelocityEventHandlerFactory;

public class ChatEventHandlerFactory extends AbstractVelocityEventHandlerFactory<PlayerChatEvent, SPlayerChatEvent> {

    public ChatEventHandlerFactory(@NotNull Object plugin, @NotNull ProxyServer proxyServer) {
        super(PlayerChatEvent.class, SPlayerChatEvent.class, plugin, proxyServer);
    }

    @Override
    protected @NotNull SPlayerChatEvent wrapEvent(@NotNull PlayerChatEvent event, @NotNull EventPriority priority) {
        return new VelocityPlayerChatEvent(event);
    }
}