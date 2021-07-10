package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerItemMendEvent;

public class PlayerItemMendEventListener extends AbstractBukkitEventHandlerFactory<PlayerItemMendEvent, SPlayerItemMendEvent> {

    public PlayerItemMendEventListener(Plugin plugin) {
        super(PlayerItemMendEvent.class, SPlayerItemMendEvent.class, plugin);
    }

    @Override
    protected SPlayerItemMendEvent wrapEvent(PlayerItemMendEvent event, EventPriority priority) {
        return new SPlayerItemMendEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                ItemFactory.build(event.getItem()).orElseThrow(),
                event.getExperienceOrb(),
                event.getRepairAmount()
        );
    }

    @Override
    protected void postProcess(SPlayerItemMendEvent wrappedEvent, PlayerItemMendEvent event) {
        event.setRepairAmount(wrappedEvent.getRepairAmount());
    }
}
