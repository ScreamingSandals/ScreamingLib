package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntitySpawnEvent;
import org.screamingsandals.lib.event.entity.SItemSpawnEvent;
import org.screamingsandals.lib.event.entity.SProjectileLaunchEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerProjectileLaunchEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class EntitySpawnEventListener extends AbstractBukkitEventHandlerFactory<EntitySpawnEvent, SEntitySpawnEvent> {

    public EntitySpawnEventListener(Plugin plugin) {
        super(EntitySpawnEvent.class, SEntitySpawnEvent.class, plugin);
    }

    @Override
    protected SEntitySpawnEvent wrapEvent(EntitySpawnEvent event, EventPriority priority) {
        if (event instanceof ItemSpawnEvent) {
            return new SItemSpawnEvent(ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()));
        }
        if (event instanceof ProjectileLaunchEvent) {
            if (((ProjectileLaunchEvent) event).getEntity().getShooter() instanceof Player) {
                return new SPlayerProjectileLaunchEvent(
                        ImmutableObjectLink.of(() -> new BukkitEntityPlayer((Player) ((ProjectileLaunchEvent) event).getEntity().getShooter())),
                        ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow())
                );
            }
            return new SProjectileLaunchEvent(ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()));
        }
        return new SEntitySpawnEvent(ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()));
    }
}
