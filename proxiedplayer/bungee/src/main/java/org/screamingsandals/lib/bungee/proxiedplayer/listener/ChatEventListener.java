package org.screamingsandals.lib.bungee.proxiedplayer.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import org.screamingsandals.lib.bungee.event.AbstractEventListener;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxiedplayer.event.PlayerChatEvent;

public class ChatEventListener extends AbstractEventListener<ChatEvent> {

    @Override
    protected void onFire(ChatEvent chatEvent, org.screamingsandals.lib.event.EventPriority eventPriority) {
        if (!(chatEvent.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        var event = new PlayerChatEvent(ProxiedPlayerMapper.wrapPlayer(chatEvent.getSender()),
                chatEvent.getMessage(), chatEvent.isCancelled(), chatEvent.isCommand());
        EventManager.getDefaultEventManager().fireEvent(event, eventPriority);
        chatEvent.setCancelled(event.isCancelled());
        chatEvent.setMessage(event.getMessage());
    }
}
