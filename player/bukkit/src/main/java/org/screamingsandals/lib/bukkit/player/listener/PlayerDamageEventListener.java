package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerDamageEvent;

public class PlayerDamageEventListener extends AbstractBukkitEventHandlerFactory<EntityDamageEvent, SPlayerDamageEvent> {

    public PlayerDamageEventListener(Plugin plugin) {
        super(EntityDamageEvent.class, SPlayerDamageEvent.class, plugin);
    }

    @Override
    protected SPlayerDamageEvent wrapEvent(EntityDamageEvent event, EventPriority priority) {
        if (event.getEntity() instanceof Player) {
            return new SPlayerDamageEvent(
                    PlayerMapper.wrapPlayer((Player)event.getEntity()),
                    SPlayerDamageEvent.DamageCause.convert(event.getCause().name()),
                    event.getDamage()
            );
        }
        return null;
    }

    @Override
    protected void postProcess(SPlayerDamageEvent wrappedEvent, EntityDamageEvent event) {
        event.setDamage(wrappedEvent.getDamage());
    }
}
