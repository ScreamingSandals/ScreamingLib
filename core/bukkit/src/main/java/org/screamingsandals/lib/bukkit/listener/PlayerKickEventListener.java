package org.screamingsandals.lib.bukkit.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerKickEvent;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.adventure.AdventureUtils;

public class PlayerKickEventListener extends AbstractBukkitEventHandlerFactory<PlayerKickEvent, SPlayerKickEvent> {

    public PlayerKickEventListener(Plugin plugin) {
        super(PlayerKickEvent.class, SPlayerKickEvent.class, plugin);
    }

    @Override
    protected SPlayerKickEvent wrapEvent(PlayerKickEvent event, EventPriority priority) {
        var leaveMessage = AdventureUtils
                .get(event, "leaveMessage")
                .ifPresentOrElseGet(classMethod ->
                                classMethod.invokeInstanceResulted(event).as(Component.class),
                        () -> AdventureHelper.toComponent(event.getLeaveMessage()));

        var reason = AdventureUtils
                .get(event, "reason")
                .ifPresentOrElseGet(classMethod ->
                                classMethod.invokeInstanceResulted(event).as(Component.class),
                        () -> AdventureHelper.toComponent(event.getReason()));

        return new SPlayerKickEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                leaveMessage,
                reason
        );
    }

    @Override
    protected void postProcess(SPlayerKickEvent wrappedEvent, PlayerKickEvent event) {
        AdventureUtils
                .get(event, "leaveMessage", Component.class)
                .ifPresentOrElse(classMethod ->
                                classMethod.invokeInstance(event, wrappedEvent.getLeaveMessage()),
                        () -> event.setLeaveMessage(AdventureHelper.toLegacy(wrappedEvent.getLeaveMessage())));
        AdventureUtils
                .get(event, "reason", Component.class)
                .ifPresentOrElse(classMethod ->
                                classMethod.invokeInstance(event, wrappedEvent.getKickReason()),
                        () -> event.setReason(AdventureHelper.toLegacy(wrappedEvent.getKickReason())));
    }
}
