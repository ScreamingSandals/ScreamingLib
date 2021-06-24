package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerItemDamageEvent;

public class PlayerItemDamageEventListener extends AbstractBukkitEventHandlerFactory<PlayerItemDamageEvent, SPlayerItemDamageEvent> {

    public PlayerItemDamageEventListener(Plugin plugin) {
        super(PlayerItemDamageEvent.class, SPlayerItemDamageEvent.class, plugin);
    }

    @Override
    protected SPlayerItemDamageEvent wrapEvent(PlayerItemDamageEvent event, EventPriority priority) {
        return new SPlayerItemDamageEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                ItemFactory.build(event.getItem()).orElseThrow(),
                event.getDamage()
        );
    }

    @Override
    protected void postProcess(SPlayerItemDamageEvent wrappedEvent, PlayerItemDamageEvent event) {
        event.setDamage(wrappedEvent.getDamage());
    }
}
