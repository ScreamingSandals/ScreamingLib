package org.screamingsandals.lib.bukkit.player.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerKickEvent;
import org.screamingsandals.lib.utils.AdventureHelper;

public class PlayerKickEventListener extends AbstractBukkitEventHandlerFactory<PlayerKickEvent, SPlayerKickEvent> {

    public PlayerKickEventListener(Plugin plugin) {
        super(PlayerKickEvent.class, SPlayerKickEvent.class, plugin);
    }

    @Override
    protected SPlayerKickEvent wrapEvent(PlayerKickEvent event, EventPriority priority) {
        Component leaveMessage, kickMessage;
        try {
            leaveMessage = event.leaveMessage();
            kickMessage = event.reason();
        } catch (Exception e) {
           leaveMessage = Component.text(event.getLeaveMessage());
           kickMessage = Component.text(event.getReason());
        }
        return new SPlayerKickEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                leaveMessage,
                kickMessage
        );
    }

    @Override
    protected void postProcess(SPlayerKickEvent wrappedEvent, PlayerKickEvent event) {
        try {
            event.leaveMessage(wrappedEvent.getLeaveMessage());
            event.reason(wrappedEvent.getKickReason());
        } catch (Exception e) {
            event.setLeaveMessage(AdventureHelper.toLegacy(wrappedEvent.getLeaveMessage()));
            event.setReason(AdventureHelper.toLegacy(wrappedEvent.getKickReason()));
        }
    }
}
