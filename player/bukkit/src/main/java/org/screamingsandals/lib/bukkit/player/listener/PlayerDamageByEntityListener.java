package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerDamageByEntityEvent;
import org.screamingsandals.lib.player.event.SPlayerDamageEvent;

public class PlayerDamageByEntityListener extends AbstractBukkitEventHandlerFactory<EntityDamageByEntityEvent, SPlayerDamageByEntityEvent> {

    public PlayerDamageByEntityListener(Plugin plugin) {
        super(EntityDamageByEntityEvent.class, SPlayerDamageByEntityEvent.class, plugin);
    }

    @Override
    protected SPlayerDamageByEntityEvent wrapEvent(EntityDamageByEntityEvent event, EventPriority priority) {
        if (event.getEntity() instanceof Player) {
            return new SPlayerDamageByEntityEvent(
                    EntityMapper.wrapEntity(event.getDamager()).orElseThrow(),
                    PlayerMapper.wrapPlayer((Player) event.getEntity()),
                    SPlayerDamageEvent.DamageCause.convert(event.getCause().name()),
                    event.getDamage()
            );
        }
        return null;
    }

    @Override
    protected void postProcess(SPlayerDamageByEntityEvent wrappedEvent, EntityDamageByEntityEvent event) {
        event.setDamage(wrappedEvent.getDamage());
    }
}
