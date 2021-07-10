package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerClickedBlockEvent;
import org.screamingsandals.lib.event.player.SPlayerInteractEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerInteractEventListener extends AbstractBukkitEventHandlerFactory<PlayerInteractEvent, SPlayerInteractEvent> {

    public PlayerInteractEventListener(Plugin plugin) {
        super(PlayerInteractEvent.class, SPlayerInteractEvent.class, plugin);
    }

    @Override
    protected SPlayerInteractEvent wrapEvent(PlayerInteractEvent event, EventPriority priority) {
        return new SPlayerInteractEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                ItemFactory.build(event.getItem()).orElse(null),
                SPlayerClickedBlockEvent.Action.convert(event.getAction().name()),
                BlockMapper.wrapBlock(event.getClickedBlock()),
                BlockFace.valueOf(event.getBlockFace().name().toUpperCase()),
                AbstractEvent.Result.convert(event.useInteractedBlock().name()),
                AbstractEvent.Result.convert(event.useItemInHand().name()),
                EquipmentSlotMapping.resolve(event.getHand()).orElseThrow()
        );
    }

    @Override
    protected void postProcess(SPlayerInteractEvent wrappedEvent, PlayerInteractEvent event) {
        event.setUseInteractedBlock(Event.Result.valueOf(wrappedEvent.getUseClickedBlock().name().toUpperCase()));
        event.setUseItemInHand(Event.Result.valueOf(wrappedEvent.getUseItemInHand().name().toUpperCase()));
    }
}
