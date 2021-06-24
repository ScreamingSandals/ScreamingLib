package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SExpBottleEvent;
import org.screamingsandals.lib.entity.event.SProjectileHitEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.BlockMapper;


public class ProjectileHitEventListener extends AbstractBukkitEventHandlerFactory<ProjectileHitEvent, SProjectileHitEvent> {
    private boolean hasExpEvent;

    public ProjectileHitEventListener(Plugin plugin) {
        super(ProjectileHitEvent.class, SProjectileHitEvent.class, plugin);
        hasExpEvent = Reflect.has("org.bukkit.event.entity.ExpBottleEvent");
    }

    @Override
    protected SProjectileHitEvent wrapEvent(ProjectileHitEvent event, EventPriority priority) {
        var hitBlockFace = event.getHitBlockFace();

        if (hasExpEvent && event instanceof ExpBottleEvent) {
            return new SExpBottleEvent(
                    EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                    EntityMapper.wrapEntity(event.getHitEntity()).orElseThrow(),
                    BlockMapper.wrapBlock(event.getHitBlock()),
                    BlockFace.valueOf(hitBlockFace != null ? hitBlockFace.name().toUpperCase() : BlockFace.UP.name().toUpperCase()),
                    ((ExpBottleEvent)event).getExperience(),
                    ((ExpBottleEvent)event).getShowEffect()
            );
        }
        return new SProjectileHitEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                EntityMapper.wrapEntity(event.getHitEntity()).orElseThrow(),
                BlockMapper.wrapBlock(event.getHitBlock()),
                BlockFace.valueOf(hitBlockFace != null ? hitBlockFace.name().toUpperCase() : BlockFace.UP.name().toUpperCase())
        );
    }

    @Override
    protected void postProcess(SProjectileHitEvent wrappedEvent, ProjectileHitEvent event) {
        if (event instanceof ExpBottleEvent) {
            var cWrappedEvent = (SExpBottleEvent) wrappedEvent;
            var cEvent = (ExpBottleEvent) event;
            cEvent.setExperience(cWrappedEvent.getExp());
            cEvent.setShowEffect(cWrappedEvent.isShowEffect());
        }
    }
}
