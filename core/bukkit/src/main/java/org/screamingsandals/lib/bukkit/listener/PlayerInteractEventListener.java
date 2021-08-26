package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerInteractEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockMapper;

public class PlayerInteractEventListener extends AbstractBukkitEventHandlerFactory<PlayerInteractEvent, SPlayerInteractEvent> {

    public PlayerInteractEventListener(Plugin plugin) {
        super(PlayerInteractEvent.class, SPlayerInteractEvent.class, plugin);
    }

    @Override
    protected SPlayerInteractEvent wrapEvent(PlayerInteractEvent event, EventPriority priority) {
        return new SPlayerInteractEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> ItemFactory.build(event.getItem()).orElse(null)),
                ImmutableObjectLink.of(() -> SPlayerInteractEvent.Action.convert(event.getAction().name())),
                ImmutableObjectLink.of(() -> BlockMapper.resolve(event.getClickedBlock()).orElse(null)),
                ImmutableObjectLink.of(() -> BlockFace.valueOf(event.getBlockFace().name().toUpperCase())),
                ObjectLink.of(
                        () -> AbstractEvent.Result.convert(event.useInteractedBlock().name()),
                        result -> event.setUseInteractedBlock(Event.Result.valueOf(result.name().toUpperCase()))
                ),
                ObjectLink.of(
                        () -> AbstractEvent.Result.convert(event.useItemInHand().name()),
                        result -> event.setUseItemInHand(Event.Result.valueOf(result.name().toUpperCase()))
                ),
                ImmutableObjectLink.of(() -> EquipmentSlotHolder.ofOptional(event.getHand()).orElse(null))
        );
    }
}
