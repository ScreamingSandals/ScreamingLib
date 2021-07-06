package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerItemHeldEvent;

public class PlayerItemHeldEventListener extends AbstractBukkitEventHandlerFactory<PlayerItemHeldEvent, SPlayerItemHeldEvent> {

    public PlayerItemHeldEventListener(Plugin plugin) {
        super(PlayerItemHeldEvent.class, SPlayerItemHeldEvent.class, plugin);
    }

    @Override
    protected SPlayerItemHeldEvent wrapEvent(PlayerItemHeldEvent event, EventPriority priority) {
        return new SPlayerItemHeldEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                event.getPreviousSlot(),
                event.getNewSlot()
        );
    }
}
