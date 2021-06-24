package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerCommandPreprocessEvent;
import org.screamingsandals.lib.utils.AdventureHelper;

public class PlayerCommandPreprocessEventListener extends AbstractBukkitEventHandlerFactory<PlayerCommandPreprocessEvent, SPlayerCommandPreprocessEvent> {

    public PlayerCommandPreprocessEventListener(Plugin plugin) {
        super(PlayerCommandPreprocessEvent.class, SPlayerCommandPreprocessEvent.class, plugin);
    }

    @Override
    protected SPlayerCommandPreprocessEvent wrapEvent(PlayerCommandPreprocessEvent event, EventPriority priority) {
        return new SPlayerCommandPreprocessEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                AdventureHelper.toComponent(event.getMessage())
        );
    }

    @Override
    protected void postProcess(SPlayerCommandPreprocessEvent wrappedEvent, PlayerCommandPreprocessEvent event) {
        event.setPlayer(wrappedEvent.getPlayer().as(Player.class));
        event.setMessage(AdventureHelper.toLegacy(wrappedEvent.getMessage()));
    }
}
