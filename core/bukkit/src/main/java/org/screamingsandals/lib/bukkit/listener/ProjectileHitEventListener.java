package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SExpBottleEvent;
import org.screamingsandals.lib.event.entity.SProjectileHitEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.block.BlockMapper;


public class ProjectileHitEventListener extends AbstractBukkitEventHandlerFactory<ProjectileHitEvent, SProjectileHitEvent> {
    private final boolean hasExpEvent;

    public ProjectileHitEventListener(Plugin plugin) {
        super(ProjectileHitEvent.class, SProjectileHitEvent.class, plugin);
        hasExpEvent = Reflect.has("org.bukkit.event.entity.ExpBottleEvent");
    }

    @Override
    protected SProjectileHitEvent wrapEvent(ProjectileHitEvent event, EventPriority priority) {
        var hitBlockFace = event.getHitBlockFace();

        if (hasExpEvent && event instanceof ExpBottleEvent) {
            return new SExpBottleEvent(
                    ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                    ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getHitEntity()).orElseThrow()),
                    ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getHitBlock())),
                    ImmutableObjectLink.of(() -> hitBlockFace != null ? BlockFace.valueOf(hitBlockFace.name().toUpperCase()) : BlockFace.UP),
                    ObjectLink.of(((ExpBottleEvent) event)::getExperience, ((ExpBottleEvent) event)::setExperience),
                    ObjectLink.of(((ExpBottleEvent) event)::getShowEffect, ((ExpBottleEvent) event)::setShowEffect)
            );
        }
        return new SProjectileHitEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getHitEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getHitBlock())),
                ImmutableObjectLink.of(() -> hitBlockFace != null ? BlockFace.valueOf(hitBlockFace.name().toUpperCase()) : BlockFace.UP)
        );
    }
}
