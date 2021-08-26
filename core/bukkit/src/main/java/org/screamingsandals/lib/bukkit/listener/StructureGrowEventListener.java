package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.block.BlockState;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.event.world.SPlantGrowEvent;
import org.screamingsandals.lib.block.state.BlockStateMapper;

public class StructureGrowEventListener extends AbstractBukkitEventHandlerFactory<StructureGrowEvent, SPlantGrowEvent> {

    public StructureGrowEventListener(Plugin plugin) {
        super(StructureGrowEvent.class, SPlantGrowEvent.class, plugin);
    }

    @Override
    protected SPlantGrowEvent wrapEvent(StructureGrowEvent event, EventPriority priority) {
        return new SPlantGrowEvent(
                new CollectionLinkedToCollection<>(event.getBlocks(), o -> o.as(BlockState.class), o -> BlockStateMapper.wrapBlockState(o).orElseThrow()),
                ImmutableObjectLink.of(() -> LocationMapper.wrapLocation(event.getLocation())),
                ImmutableObjectLink.of(() -> event.getPlayer() != null ? PlayerMapper.wrapPlayer(event.getPlayer()) : null),
                ImmutableObjectLink.of(event::isFromBonemeal)
        );
    }
}
