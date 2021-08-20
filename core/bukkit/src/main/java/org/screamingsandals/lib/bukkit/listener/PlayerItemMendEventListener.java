package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityExperience;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerItemMendEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class PlayerItemMendEventListener extends AbstractBukkitEventHandlerFactory<PlayerItemMendEvent, SPlayerItemMendEvent> {

    public PlayerItemMendEventListener(Plugin plugin) {
        super(PlayerItemMendEvent.class, SPlayerItemMendEvent.class, plugin);
    }

    @Override
    protected SPlayerItemMendEvent wrapEvent(PlayerItemMendEvent event, EventPriority priority) {
        return new SPlayerItemMendEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> ItemFactory.build(event.getItem()).orElseThrow()),
                ImmutableObjectLink.of(() -> EntityMapper.<EntityExperience>wrapEntity(event.getExperienceOrb()).orElseThrow()),
                ObjectLink.of(event::getRepairAmount, event::setRepairAmount)
        );
    }
}
