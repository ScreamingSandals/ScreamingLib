package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerRightClickedEntityEvent;

public class PlayerRightClickedEntityListener extends AbstractBukkitEventHandlerFactory<PlayerInteractEntityEvent, SPlayerRightClickedEntityEvent> {

    public PlayerRightClickedEntityListener(Plugin plugin) {
        super(PlayerInteractEntityEvent.class, SPlayerRightClickedEntityEvent.class, plugin);
    }

    @Override
    protected SPlayerRightClickedEntityEvent wrapEvent(PlayerInteractEntityEvent event, EventPriority priority) {
        return new SPlayerRightClickedEntityEvent(PlayerMapper.wrapPlayer(event.getPlayer()),
                PlayerMapper.wrapHand(event.getHand()),
                EntityMapper.wrapEntity(event.getRightClicked()).orElseThrow()
        );
    }
}
