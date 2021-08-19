package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityPortalExitEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.utils.math.Vector3D;

public class EntityPortalEnterExitListener extends AbstractBukkitEventHandlerFactory<EntityPortalExitEvent, SEntityPortalExitEvent> {

    public EntityPortalEnterExitListener(Plugin plugin) {
        super(EntityPortalExitEvent.class, SEntityPortalExitEvent.class, plugin);
    }

    @Override
    protected SEntityPortalExitEvent wrapEvent(EntityPortalExitEvent event, EventPriority priority) {
        return new SEntityPortalExitEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> new Vector3D(event.getBefore().getX(), event.getBefore().getY(), event.getBefore().getZ())),
                ObjectLink.of(
                        () -> new Vector3D(event.getAfter().getX(), event.getAfter().getY(), event.getAfter().getZ()),
                        vector3D -> event.setAfter(new Vector(vector3D.getX(), vector3D.getY(), vector3D.getZ()))
                )
        );
    }
}
