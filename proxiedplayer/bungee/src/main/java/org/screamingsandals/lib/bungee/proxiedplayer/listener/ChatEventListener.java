package org.screamingsandals.lib.bungee.proxiedplayer.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxiedplayer.event.PlayerChatEvent;
import org.screamingsandals.lib.utils.event.EventManager;

public class ChatEventListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLowest(ChatEvent chatEvent) {
        fireEvent(chatEvent, org.screamingsandals.lib.utils.event.EventPriority.LOWEST);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLow(ChatEvent chatEvent) {
        fireEvent(chatEvent, org.screamingsandals.lib.utils.event.EventPriority.LOW);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNormal(ChatEvent chatEvent) {
        fireEvent(chatEvent, org.screamingsandals.lib.utils.event.EventPriority.NORMAL);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHigh(ChatEvent chatEvent) {
        fireEvent(chatEvent, org.screamingsandals.lib.utils.event.EventPriority.HIGH);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHighest(ChatEvent chatEvent) {
        fireEvent(chatEvent, org.screamingsandals.lib.utils.event.EventPriority.HIGHEST);
    }

    private void fireEvent(ChatEvent chatEvent, org.screamingsandals.lib.utils.event.EventPriority eventPriority) {
        if (!(chatEvent.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        var event = new PlayerChatEvent(ProxiedPlayerMapper.wrapPlayer(chatEvent.getSender()), chatEvent.getMessage(), chatEvent.isCancelled(), chatEvent.isCommand());
        EventManager.getBaseEventManager().fireEvent(event, eventPriority);
        chatEvent.setCancelled(event.isCancelled());
        chatEvent.setMessage(event.getMessage());
    }
}
