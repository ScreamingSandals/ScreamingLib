package org.screamingsandals.lib.bukkit.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerJoinEvent;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.adventure.AdventureUtils;

public class PlayerJoinEventListener extends AbstractBukkitEventHandlerFactory<PlayerJoinEvent, SPlayerJoinEvent> {

    public PlayerJoinEventListener(Plugin plugin) {
        super(PlayerJoinEvent.class, SPlayerJoinEvent.class, plugin);
    }

    @Override
    protected SPlayerJoinEvent wrapEvent(PlayerJoinEvent event, EventPriority priority) {
        return new SPlayerJoinEvent(PlayerMapper.wrapPlayer(event.getPlayer()), AdventureUtils
                .get(event, "joinMessage")
                .ifPresentOrElseGet(classMethod ->
                                classMethod.invokeInstanceResulted(event).as(Component.class),
                        () -> AdventureHelper.toComponentNullableResult(event.getJoinMessage())));
    }

    @Override
    protected void postProcess(SPlayerJoinEvent wrappedEvent, PlayerJoinEvent event) {
        AdventureUtils
                .get(event, "joinMessage", Component.class)
                .ifPresentOrElse(classMethod -> classMethod.invokeInstance(event, wrappedEvent.getJoinMessage()),
                        () -> event.setJoinMessage(AdventureHelper.toLegacyNullableResult(wrappedEvent.getJoinMessage())));
    }
}
