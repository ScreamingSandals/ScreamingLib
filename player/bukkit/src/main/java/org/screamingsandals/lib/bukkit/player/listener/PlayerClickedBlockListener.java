package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerClickedBlockEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerClickedBlockListener extends AbstractBukkitEventHandlerFactory<PlayerInteractEvent, SPlayerClickedBlockEvent> {

    public PlayerClickedBlockListener(Plugin plugin) {
        super(PlayerInteractEvent.class, SPlayerClickedBlockEvent.class, plugin);
    }

    @Override
    protected SPlayerClickedBlockEvent wrapEvent(PlayerInteractEvent event, EventPriority priority) {
        return new SPlayerClickedBlockEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                PlayerMapper.resolveHand(event.getHand()).orElse(null),
                SPlayerClickedBlockEvent.Action.convert(event.getAction().name()),
                ItemFactory.build(event.getItem()).orElse(null),
                BlockMapper.resolve(event.getClickedBlock()).orElse(null),
                BlockFace.valueOf(event.getBlockFace().name())
        );
    }
}
