package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerGameModeChangeEvent;
import org.screamingsandals.lib.utils.GameMode;

public class PlayerGameModeChangeEventListener extends AbstractBukkitEventHandlerFactory<PlayerGameModeChangeEvent, SPlayerGameModeChangeEvent> {

    public PlayerGameModeChangeEventListener(Plugin plugin) {
        super(PlayerGameModeChangeEvent.class, SPlayerGameModeChangeEvent.class, plugin);
    }

    @Override
    protected SPlayerGameModeChangeEvent wrapEvent(PlayerGameModeChangeEvent event, EventPriority priority) {
        return new SPlayerGameModeChangeEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                GameMode.convert(event.getNewGameMode().name())
        );
    }
}
