package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerInteractAtEntityEvent;

public class PlayerInteractAtEntityEventListener extends AbstractBukkitEventHandlerFactory<PlayerInteractAtEntityEvent, SPlayerInteractAtEntityEvent> {

    public PlayerInteractAtEntityEventListener(Plugin plugin) {
        super(PlayerInteractAtEntityEvent.class, SPlayerInteractAtEntityEvent.class, plugin);
    }

    @Override
    protected SPlayerInteractAtEntityEvent wrapEvent(PlayerInteractAtEntityEvent event, EventPriority priority) {
        return new SPlayerInteractAtEntityEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                EntityMapper.wrapEntity(event.getRightClicked()).orElseThrow(),
                EquipmentSlotMapping.resolve(event.getHand()).orElseThrow()
        );
    }
}
