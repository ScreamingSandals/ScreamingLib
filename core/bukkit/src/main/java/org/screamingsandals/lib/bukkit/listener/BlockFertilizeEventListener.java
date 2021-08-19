package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockFertilizeEvent;
import org.screamingsandals.lib.world.state.BlockStateMapper;

public class BlockFertilizeEventListener extends AbstractBukkitEventHandlerFactory<BlockFertilizeEvent, SBlockFertilizeEvent> {

    public BlockFertilizeEventListener(Plugin plugin) {
        super(BlockFertilizeEvent.class, SBlockFertilizeEvent.class, plugin);
    }

    @Override
    protected SBlockFertilizeEvent wrapEvent(BlockFertilizeEvent event, EventPriority priority) {
        return new SBlockFertilizeEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                new CollectionLinkedToCollection<>(event.getBlocks(), o -> o.as(BlockState.class), o -> BlockStateMapper.wrapBlockState(o).orElseThrow())
        );
    }
}
