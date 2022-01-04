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

package org.screamingsandals.lib.bungee.proxy.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import org.screamingsandals.lib.bungee.event.AbstractEventListener;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.proxy.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxy.event.SPlayerChatEvent;

public class ChatEventListener extends AbstractEventListener<ChatEvent> {

    @Override
    protected void onFire(ChatEvent chatEvent, EventPriority eventPriority) {
        if (!(chatEvent.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        var event = new SPlayerChatEvent(
                ProxiedPlayerMapper.wrapPlayer(chatEvent.getSender()),
                chatEvent.isCommand(),
                chatEvent.getMessage(),
                chatEvent.isCancelled()
        );

        EventManager.getDefaultEventManager().fireEvent(event, eventPriority);
        chatEvent.setCancelled(event.isCancelled());
        chatEvent.setMessage(event.getMessage());
    }
}
