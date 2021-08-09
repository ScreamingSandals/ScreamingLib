package org.screamingsandals.lib.bukkit.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.adventure.AdventureUtils;

public class PlayerLeaveEventListener extends AbstractBukkitEventHandlerFactory<PlayerQuitEvent, SPlayerLeaveEvent> {

    public PlayerLeaveEventListener(Plugin plugin) {
        super(PlayerQuitEvent.class, SPlayerLeaveEvent.class, plugin);
    }

    @Override
    protected SPlayerLeaveEvent wrapEvent(PlayerQuitEvent event, EventPriority priority) {
        return new SPlayerLeaveEvent(PlayerMapper.wrapPlayer(event.getPlayer()), AdventureUtils
                .get(event, "quitMessage")
                .ifPresentOrElseGet(classMethod ->
                                classMethod.invokeInstanceResulted(event).as(Component.class),
                        () -> AdventureHelper.toComponentNullableResult(event.getQuitMessage())));
    }

    @Override
    protected void postProcess(SPlayerLeaveEvent wrappedEvent, PlayerQuitEvent event) {
        AdventureUtils
                .get(event, "quitMessage", Component.class)
                .ifPresentOrElse(classMethod -> classMethod.invokeInstance(event, wrappedEvent.getLeaveMessage()),
                        () -> event.setQuitMessage(AdventureHelper.toLegacyNullableResult(wrappedEvent.getLeaveMessage())));
    }
}
