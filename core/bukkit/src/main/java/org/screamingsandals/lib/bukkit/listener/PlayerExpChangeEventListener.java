package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerExpChangeEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class PlayerExpChangeEventListener extends AbstractBukkitEventHandlerFactory<PlayerExpChangeEvent, SPlayerExpChangeEvent> {

    public PlayerExpChangeEventListener(Plugin plugin) {
        super(PlayerExpChangeEvent.class, SPlayerExpChangeEvent.class, plugin);
    }

    @Override
    protected SPlayerExpChangeEvent wrapEvent(PlayerExpChangeEvent event, EventPriority priority) {
        return new SPlayerExpChangeEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ObjectLink.of(event::getAmount, event::setAmount)
        );
    }
}
