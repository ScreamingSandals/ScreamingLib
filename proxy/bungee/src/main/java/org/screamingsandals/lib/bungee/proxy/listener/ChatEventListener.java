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
