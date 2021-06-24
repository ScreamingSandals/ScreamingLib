package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerLevelChangeEvent;

public class PlayerLevelChangeEventListener extends AbstractBukkitEventHandlerFactory<PlayerLevelChangeEvent, SPlayerLevelChangeEvent> {

    public PlayerLevelChangeEventListener(Plugin plugin) {
        super(PlayerLevelChangeEvent.class, SPlayerLevelChangeEvent.class, plugin);
    }

    @Override
    protected SPlayerLevelChangeEvent wrapEvent(PlayerLevelChangeEvent event, EventPriority priority) {
        return new SPlayerLevelChangeEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                event.getOldLevel(),
                event.getNewLevel()
        );
    }
}
