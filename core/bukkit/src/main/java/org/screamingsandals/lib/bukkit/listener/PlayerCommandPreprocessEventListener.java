package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerCommandPreprocessEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class PlayerCommandPreprocessEventListener extends AbstractBukkitEventHandlerFactory<PlayerCommandPreprocessEvent, SPlayerCommandPreprocessEvent> {

    public PlayerCommandPreprocessEventListener(Plugin plugin) {
        super(PlayerCommandPreprocessEvent.class, SPlayerCommandPreprocessEvent.class, plugin);
    }

    @Override
    protected SPlayerCommandPreprocessEvent wrapEvent(PlayerCommandPreprocessEvent event, EventPriority priority) {
        return new SPlayerCommandPreprocessEvent(
                ImmutableObjectLink.of(
                        () -> PlayerMapper.wrapPlayer(event.getPlayer())
                ),
                ObjectLink.of(event::getMessage, event::setMessage)
        );
    }
}
