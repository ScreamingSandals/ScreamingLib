package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerBlockPlaceEvent;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.world.state.BlockStateMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerBlockPlaceEventListener extends AbstractBukkitEventHandlerFactory<BlockPlaceEvent, SPlayerBlockPlaceEvent> {

    public PlayerBlockPlaceEventListener(Plugin plugin) {
        super(BlockPlaceEvent.class, SPlayerBlockPlaceEvent.class, plugin);
    }

    @Override
    protected SPlayerBlockPlaceEvent wrapEvent(BlockPlaceEvent event, EventPriority priority) {
        var replacedState = BlockStateMapper.wrapBlockState(event.getBlockReplacedState()).orElseThrow();
        return new SPlayerBlockPlaceEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                PlayerMapper.wrapHand(event.getHand()),
                BlockMapper.wrapBlock(event.getBlock()),
                replacedState,
                ItemFactory.build(event.getItemInHand()).orElseThrow(),
                PlayerMapper.wrapHand(event.getHand()),
                event instanceof BlockMultiPlaceEvent ? ((BlockMultiPlaceEvent) event).getReplacedBlockStates()
                        .stream().map(BlockStateMapper::wrapBlockState)
                        .map(Optional::orElseThrow)
                        .collect(Collectors.toList()) : List.of(replacedState)
        );
    }
}
