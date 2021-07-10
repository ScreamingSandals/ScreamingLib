package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerShearEntityEvent;

public class PlayerShearEntityEventListener extends AbstractBukkitEventHandlerFactory<PlayerShearEntityEvent, SPlayerShearEntityEvent> {

    public PlayerShearEntityEventListener(Plugin plugin) {
        super(PlayerShearEntityEvent.class, SPlayerShearEntityEvent.class, plugin);
    }

    @Override
    protected SPlayerShearEntityEvent wrapEvent(PlayerShearEntityEvent event, EventPriority priority) {
        return new SPlayerShearEntityEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                ItemFactory.build(event.getItem()).orElse(null),
                EquipmentSlotMapping.resolve(event.getHand()).orElse(null)
        );
    }
}
