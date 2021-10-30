package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerBedLeaveEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockMapper;

public class PlayerBedLeaveEventListener extends AbstractBukkitEventHandlerFactory<PlayerBedLeaveEvent, SPlayerBedLeaveEvent> {

    public PlayerBedLeaveEventListener(Plugin plugin) {
        super(PlayerBedLeaveEvent.class, SPlayerBedLeaveEvent.class, plugin);
    }

    @Override
    protected SPlayerBedLeaveEvent wrapEvent(PlayerBedLeaveEvent event, EventPriority priority) {
        return new SPlayerBedLeaveEvent(
                ImmutableObjectLink.of(() -> new BukkitEntityPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBed())),
                ObjectLink.of(event::shouldSetSpawnLocation, event::setSpawnLocation)
        );
    }
}
