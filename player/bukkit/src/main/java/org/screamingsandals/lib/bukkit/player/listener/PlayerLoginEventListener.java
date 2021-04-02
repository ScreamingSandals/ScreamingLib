package org.screamingsandals.lib.bukkit.player.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SAsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.player.event.SPlayerLoginEvent;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.adventure.AdventureUtils;

public class PlayerLoginEventListener extends AbstractBukkitEventHandlerFactory<PlayerLoginEvent, SPlayerLoginEvent> {

    public PlayerLoginEventListener(Plugin plugin) {
        super(PlayerLoginEvent.class, SPlayerLoginEvent.class, plugin);
    }

    @Override
    protected SPlayerLoginEvent wrapEvent(PlayerLoginEvent event, EventPriority priority) {
        var kickMessage = AdventureUtils
                .get(event, "kickMessage")
                .ifPresentOrElseGet(classMethod ->
                                classMethod.invokeInstanceResulted(event).as(Component.class),
                        () -> AdventureHelper.toComponent(event.getKickMessage()));

        return new SPlayerLoginEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                event.getAddress(),
                event.getHostname(),
                SAsyncPlayerPreLoginEvent.Result.valueOf(event.getResult().name().toUpperCase()),
                kickMessage
        );
    }

    @Override
    protected void postProcess(SPlayerLoginEvent wrappedEvent, PlayerLoginEvent event) {
        AdventureUtils
                .get(event, "kickMessage", Component.class)
                .ifPresentOrElse(classMethod ->
                                classMethod.invokeInstance(event, wrappedEvent.getMessage()),
                        () ->
                                event.setKickMessage(AdventureHelper.toLegacy(wrappedEvent.getMessage())));
        event.setResult(PlayerLoginEvent.Result.valueOf(wrappedEvent.getResult().name().toUpperCase()));
    }
}
