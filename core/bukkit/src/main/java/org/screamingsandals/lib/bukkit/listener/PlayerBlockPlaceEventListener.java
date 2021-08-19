package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerBlockPlaceEvent;
import org.screamingsandals.lib.utils.ImmutableCollectionLinkedToCollection;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.world.state.BlockStateMapper;

import java.util.List;

public class PlayerBlockPlaceEventListener extends AbstractBukkitEventHandlerFactory<BlockPlaceEvent, SPlayerBlockPlaceEvent> {

    public PlayerBlockPlaceEventListener(Plugin plugin) {
        super(BlockPlaceEvent.class, SPlayerBlockPlaceEvent.class, plugin);
    }

    @Override
    protected SPlayerBlockPlaceEvent wrapEvent(BlockPlaceEvent event, EventPriority priority) {
        return new SPlayerBlockPlaceEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> PlayerMapper.wrapHand(event.getHand())),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> BlockStateMapper.wrapBlockState(event.getBlockReplacedState()).orElseThrow()),
                ImmutableObjectLink.of(() -> ItemFactory.build(event.getItemInHand()).orElseThrow()),
                new ImmutableCollectionLinkedToCollection<>(
                        event instanceof BlockMultiPlaceEvent ? ((BlockMultiPlaceEvent) event).getReplacedBlockStates() : List.of(event.getBlockReplacedState()),
                        blockStateHolder -> blockStateHolder.as(BlockState.class),
                        blockState -> BlockStateMapper.wrapBlockState(blockState).orElseThrow()
                )
        );
    }
}
