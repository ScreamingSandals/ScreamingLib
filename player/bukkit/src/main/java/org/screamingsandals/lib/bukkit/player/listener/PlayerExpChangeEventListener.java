package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerExpChangeEvent;

public class PlayerExpChangeEventListener extends AbstractBukkitEventHandlerFactory<PlayerExpChangeEvent, SPlayerExpChangeEvent> {

    public PlayerExpChangeEventListener(Plugin plugin) {
        super(PlayerExpChangeEvent.class, SPlayerExpChangeEvent.class, plugin);
    }

    @Override
    protected SPlayerExpChangeEvent wrapEvent(PlayerExpChangeEvent event, EventPriority priority) {
        return new SPlayerExpChangeEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                event.getAmount()
        );
    }

    @Override
    protected void postProcess(SPlayerExpChangeEvent wrappedEvent, PlayerExpChangeEvent event) {
        event.setAmount(wrappedEvent.getExp());
    }
}
