package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerProjectileLaunchEvent;

public class PlayerProjectileLaunchEventListener extends AbstractBukkitEventHandlerFactory<ProjectileLaunchEvent, SPlayerProjectileLaunchEvent> {

    public PlayerProjectileLaunchEventListener(Plugin plugin) {
        super(ProjectileLaunchEvent.class, SPlayerProjectileLaunchEvent.class, plugin);
    }

    @Override
    protected SPlayerProjectileLaunchEvent wrapEvent(ProjectileLaunchEvent event, EventPriority priority) {
        if (event.getEntity().getShooter() instanceof Player) {
            return new SPlayerProjectileLaunchEvent(
                    PlayerMapper.wrapPlayer((Player)event.getEntity().getShooter()),
                    EntityMapper.wrapEntity(event.getEntity()).orElseThrow()
            );
        }
        return null;
    }

}
