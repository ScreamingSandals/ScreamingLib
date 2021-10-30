package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerArmorStandManipulateEvent;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.event.player.SPlayerInteractAtEntityEvent;
import org.screamingsandals.lib.event.player.SPlayerInteractEntityEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class PlayerInteractEntityEventListener extends AbstractBukkitEventHandlerFactory<PlayerInteractEntityEvent, SPlayerInteractEntityEvent> {

    private final boolean hasArmorStandManipulateEvent = Reflect.has("org.bukkit.event.player.PlayerArmorStandManipulateEvent");

    // Because PlayerInteractAtEntityEvent has another Handler list instance than PlayerInteractEntityEvent, we need to create this listener multiple times (fucking bukkit)
    @SuppressWarnings("unchecked")
    public PlayerInteractEntityEventListener(Plugin plugin, Class<? extends PlayerInteractEntityEvent> clazz) {
        super((Class<PlayerInteractEntityEvent>) clazz, SPlayerInteractEntityEvent.class, plugin);
    }

    @Override
    protected SPlayerInteractEntityEvent wrapEvent(PlayerInteractEntityEvent event, EventPriority priority) {
        if (hasArmorStandManipulateEvent && event instanceof PlayerArmorStandManipulateEvent) {
            return new SPlayerArmorStandManipulateEvent(
                    ImmutableObjectLink.of(() -> new BukkitEntityPlayer(event.getPlayer())),
                    ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getRightClicked()).orElseThrow()),
                    ImmutableObjectLink.of(() -> ItemFactory.build(((PlayerArmorStandManipulateEvent) event).getPlayerItem()).orElseThrow()),
                    ImmutableObjectLink.of(() -> ItemFactory.build(((PlayerArmorStandManipulateEvent) event).getArmorStandItem()).orElseThrow()),
                    ImmutableObjectLink.of(() -> EquipmentSlotHolder.of(((PlayerArmorStandManipulateEvent) event).getSlot())),
                    ImmutableObjectLink.of(() -> EquipmentSlotHolder.of(event.getHand()))
            );
        }

        if (event instanceof PlayerInteractAtEntityEvent) {
            return new SPlayerInteractAtEntityEvent(
                    ImmutableObjectLink.of(() -> new BukkitEntityPlayer(event.getPlayer())),
                    ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getRightClicked()).orElseThrow()),
                    ImmutableObjectLink.of(() -> EquipmentSlotHolder.of(event.getHand()))
            );
        }
        return new SPlayerInteractEntityEvent(
                ImmutableObjectLink.of(() -> new BukkitEntityPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getRightClicked()).orElseThrow()),
                ImmutableObjectLink.of(() -> EquipmentSlotHolder.of(event.getHand()))
        );
    }
}
