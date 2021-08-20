package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerItemDamageEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class PlayerItemDamageEventListener extends AbstractBukkitEventHandlerFactory<PlayerItemDamageEvent, SPlayerItemDamageEvent> {

    public PlayerItemDamageEventListener(Plugin plugin) {
        super(PlayerItemDamageEvent.class, SPlayerItemDamageEvent.class, plugin);
    }

    @Override
    protected SPlayerItemDamageEvent wrapEvent(PlayerItemDamageEvent event, EventPriority priority) {
        return new SPlayerItemDamageEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> ItemFactory.build(event.getItem()).orElseThrow()),
                ObjectLink.of(event::getDamage, event::setDamage)
        );
    }
}
