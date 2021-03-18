package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerBedLeaveEvent;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerBedLeaveEventListener extends AbstractBukkitEventHandlerFactory<PlayerBedLeaveEvent, SPlayerBedLeaveEvent> {

    public PlayerBedLeaveEventListener(Plugin plugin) {
        super(PlayerBedLeaveEvent.class, SPlayerBedLeaveEvent.class, plugin);
    }

    @Override
    protected SPlayerBedLeaveEvent wrapEvent(PlayerBedLeaveEvent event, EventPriority priority) {
        return new SPlayerBedLeaveEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                BlockMapper.wrapBlock(event.getBed()),
                event.shouldSetSpawnLocation()
        );
    }
}
