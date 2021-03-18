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

public class PlayerLoginEventListener extends AbstractBukkitEventHandlerFactory<PlayerLoginEvent, SPlayerLoginEvent> {

    public PlayerLoginEventListener(Plugin plugin) {
        super(PlayerLoginEvent.class, SPlayerLoginEvent.class, plugin);
    }

    @Override
    protected SPlayerLoginEvent wrapEvent(PlayerLoginEvent event, EventPriority priority) {
        Component kickMessage;
        try {
            kickMessage = event.kickMessage();
        } catch (Exception e) {
            kickMessage = AdventureHelper.toComponent(event.getKickMessage());
        }
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
        try {
            event.kickMessage(wrappedEvent.getMessage());
        } catch (Exception e) {
            event.setKickMessage(AdventureHelper.toLegacy(wrappedEvent.getMessage()));
        }
        event.setResult(PlayerLoginEvent.Result.valueOf(wrappedEvent.getResult().name().toUpperCase()));
    }
}
