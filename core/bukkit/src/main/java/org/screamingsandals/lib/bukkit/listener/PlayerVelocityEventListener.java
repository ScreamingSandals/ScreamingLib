package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerVelocityChangeEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.utils.math.Vector3D;

public class PlayerVelocityEventListener extends AbstractBukkitEventHandlerFactory<PlayerVelocityEvent, SPlayerVelocityChangeEvent> {

    public PlayerVelocityEventListener(Plugin plugin) {
        super(PlayerVelocityEvent.class, SPlayerVelocityChangeEvent.class, plugin);
    }

    @Override
    protected SPlayerVelocityChangeEvent wrapEvent(PlayerVelocityEvent event, EventPriority priority) {
        return new SPlayerVelocityChangeEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ObjectLink.of(
                        () -> new Vector3D(event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ()),
                        vector3D -> event.setVelocity(new Vector(vector3D.getX(), vector3D.getY(), vector3D.getZ()))
                )
        );
    }
}
