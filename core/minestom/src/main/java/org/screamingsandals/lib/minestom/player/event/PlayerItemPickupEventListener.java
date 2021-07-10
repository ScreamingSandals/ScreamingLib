package org.screamingsandals.lib.minestom.player.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.item.PickupItemEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerPickupItemEvent;

public class PlayerItemPickupEventListener {

    public PlayerItemPickupEventListener() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(PickupItemEvent.class, event -> {
            final var entity = event.getLivingEntity();
            if (entity instanceof Player) {
                EventManager.fire(new SPlayerPickupItemEvent(
                        PlayerMapper.wrapPlayer((Player) entity),
                        ItemFactory.build(event.getItemStack()).orElseThrow())
                );
            }
        });
    }
}
