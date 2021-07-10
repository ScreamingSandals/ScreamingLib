package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerArmorStandManipulateEvent;

public class PlayerArmorStandManipulateEventListener extends AbstractBukkitEventHandlerFactory<PlayerArmorStandManipulateEvent, SPlayerArmorStandManipulateEvent> {

    public PlayerArmorStandManipulateEventListener(Plugin plugin) {
        super(PlayerArmorStandManipulateEvent.class, SPlayerArmorStandManipulateEvent.class, plugin);
    }

    @Override
    protected SPlayerArmorStandManipulateEvent wrapEvent(PlayerArmorStandManipulateEvent event, EventPriority priority) {
        return new SPlayerArmorStandManipulateEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                EntityMapper.wrapEntity(event.getRightClicked()).orElseThrow(),
                ItemFactory.build(event.getPlayerItem()).orElseThrow(),
                ItemFactory.build(event.getArmorStandItem()).orElseThrow(),
                EquipmentSlotMapping.resolve(event.getSlot()).orElseThrow()
        );
    }
}
