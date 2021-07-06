package org.screamingsandals.lib.bukkit.listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerDropItemEvent;

public class PlayerDropItemEventListener extends AbstractBukkitEventHandlerFactory<PlayerDropItemEvent, SPlayerDropItemEvent> {
    public PlayerDropItemEventListener(Plugin plugin) {
        super(PlayerDropItemEvent.class, SPlayerDropItemEvent.class, plugin);
    }

    @Override
    protected SPlayerDropItemEvent wrapEvent(PlayerDropItemEvent event, EventPriority priority) {
        return new SPlayerDropItemEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                ItemFactory.build(event.getItemDrop()).orElseThrow()
        );
    }
}
