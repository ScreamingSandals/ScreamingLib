package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerItemConsumeEvent;

public class PlayerItemConsumeEventListener extends AbstractBukkitEventHandlerFactory<PlayerItemConsumeEvent, SPlayerItemConsumeEvent> {

    public PlayerItemConsumeEventListener(Plugin plugin) {
        super(PlayerItemConsumeEvent.class, SPlayerItemConsumeEvent.class, plugin);
    }

    @Override
    protected SPlayerItemConsumeEvent wrapEvent(PlayerItemConsumeEvent event, EventPriority priority) {
        return new SPlayerItemConsumeEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                ItemFactory.build(event.getItem()).orElseThrow()
        );
    }

    @Override
    protected void postProcess(SPlayerItemConsumeEvent wrappedEvent, PlayerItemConsumeEvent event) {
        event.setItem(wrappedEvent.getItem().as(ItemStack.class));
    }
}
